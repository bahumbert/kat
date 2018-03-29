/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idfor.kat.tools.UIBackend.service.utils;

import org.apache.commons.lang.WordUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton dédié à l'introspection d'objets.
 */
public class Reflector
{
    private static Reflector INSTANCE = null;

    /**
     * L'objet courant utilisé
     */
    private Object currentObject = null;

    /**
     * Le package de l'objet courant
     */
    private Package currentPackage = null;

    /**
     * La classe de l'objet courant
     */
    private Class<?> currentClass = null;

    /**
     * La liste de classes de l'objet courant
     */
    private HashMap<String, Class<?>> currentClassMap;

    /**
     * La liste des interfaces implémentées de l'objet courant
     */
    private HashMap<String, Type> currentInterfaceMap;

    /**
     * La liste des constructeurs de l'objet courant
     */
    private HashMap<String, Constructor<?>> currentConstructorMap;

    /**
     * La liste des méthodes de l'objet courant
     */
    private HashMap<String, Method> currentMethodMap;

    /**
     * La liste des champs de l'objet courant
     */
    private HashMap<String, Field> currentFieldMap;

    /**
     * Le serializer à utiliser
     */
    private ReflectorSerializer serializer;

    /**
     * Le constructeur de notre service
     */
    private Reflector()
    {
        this.currentClassMap = new HashMap<>();
        this.currentInterfaceMap = new HashMap<>();
        this.currentConstructorMap = new HashMap<>();
        this.currentMethodMap = new HashMap<>();
        this.currentFieldMap = new HashMap<>();

        this.serializer = new ReflectorDumperConsole();
    }

    /**
     * Délègue la gestion de l'introspection à notre "singleton".
     *
     * @param obj L'object à traiter
     * @param forceNewInstance
     * @return La réflection de l'objet
     */
    public static Reflector handle(Object obj, boolean forceNewInstance)
    {
        Reflector r;

        if(!forceNewInstance && INSTANCE == null)
        {
            r = INSTANCE = new Reflector();
        }
        else if(!forceNewInstance)
        {
            r = INSTANCE;
        }
        else
        {
            r = new Reflector();
        }

        r.currentObject = obj;
        r.init();

        return r;
    }

    /**
     * Délègue la gestion de l'introspection à notre "singleton".
     *
     * @param obj L'object à traiter
     * @return La réflection de l'objet
     */
    public static Reflector handle(Object obj)
    {
        return Reflector.handle(obj, false);
    }

    /**
     * Retrouve la valeur d'un champ pour un objet donné.
     *
     * @param field Le champ donc on veut la valeur
     * @param obj L'objet dnt on extrait la valeur
     * @return la veleur ou null
     */
    public static Object getFieldValue(Field field, Object obj)
    {
        // Si la classe du champ est bien du même type que l'objet
        if(!field.getDeclaringClass().isInstance(obj))
        {
            return null;
        }

        Class<?> objClass = obj.getClass();
        int modifiers = field.getModifiers();

        String name = field.getName();
        String nameUc = WordUtils.capitalizeFully(name, new char[]{'_'}).replaceAll("_", "");

        Object result = null;

        try {
            if(Modifier.isPublic(modifiers))
            {
                result = field.get(obj);
            }
            else if(Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers))
            {
                String[] getters = new String[]{
                    name, nameUc,
                    "get"+ nameUc, "has"+ nameUc, "is"+ nameUc,
                    "get"+ name, "has"+ name, "is"+ name
                };

                for(String methodName : getters)
                {
                    try
                    {
                        result = objClass.getMethod(methodName).invoke(obj);
                        break;
                    }
                    catch (Throwable ignored) {}
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }

    /**
     * Initialise l'instance actuelle avec l'objet courant.
     */
    private void init()
    {
        this.clear();

        try
        {
            ArrayList<Class<?>> superClassList = new ArrayList<>();
            Class<?> superClass, currentSuperClass;

            // On récupère la classe de l'objet
            this.currentClass = this.currentObject.getClass();

            // On récupère le package de l'objet
            this.currentPackage = this.currentClass.getPackage();

            // On ajoute la classe courante en début de liste
            superClassList.add( this.currentClass );
            currentSuperClass = this.currentClass;

            // Tant qu'on a des mamans !
            while((superClass = currentSuperClass.getSuperclass()) != null)
            {
                superClassList.add( superClass );
                currentSuperClass = superClass;
            }

            // On inverse la liste
            Collections.reverse(superClassList);

            // On récupère la liste des classes dans l'ordre désiré
            for(Class<?> c : superClassList)
            {
                this.currentClassMap.put(c.getCanonicalName(), c);

                // On enregistre les éléments publics déclarés
                this.registerClassElements(c);
            }

            // On récupère la liste des interfaces
            for(Type t : this.currentClass.getGenericInterfaces())
            {
                this.currentInterfaceMap.put(t.getTypeName(), t);
            }

            // On récupère la liste des constructeurs
            for(Constructor<?> c : this.currentClass.getDeclaredConstructors())
            {
                this.currentConstructorMap.put(this.getExecutableKey(c), c);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    /**
     * Nettoie l'instance actuelle.
     */
    private void clear()
    {
        this.currentClass = null;
        this.currentPackage = null;

        this.currentClassMap.clear();
        this.currentInterfaceMap.clear();
        this.currentConstructorMap.clear();
        this.currentMethodMap.clear();
        this.currentFieldMap.clear();
    }

    /**
     * Enregistre les différents éléments publics déclarés dans la classe donnée.
     *
     * @param targetClass La classe à traiter
     */
    private void registerClassElements(Class<?> targetClass)
    {
        // On récupère la liste des méthodes publiques
        for(Method m : targetClass.getDeclaredMethods())
        {
            this.currentMethodMap.put(this.getExecutableKey(m), m);
        }

        // On récupère la liste des méthodes publiques
        for(Method m : targetClass.getMethods())
        {
            this.currentMethodMap.put(this.getExecutableKey(m), m);
        }

        // On récupère la liste des champs publics
        for(Field f : targetClass.getDeclaredFields())
        {
            this.currentFieldMap.put(f.getName(), f);
        }

        // On récupère la liste des champs publics
        for(Field f : targetClass.getFields())
        {
            this.currentFieldMap.put(f.getName(), f);
        }
    }

    /**
     * Appelle une méthode avec les arguments donnés.
     *
     * @param methodName Le nom de la méthode à appeler
     * @param args La liste des arguments à passer à la méthode
     * @return Le résultat de l'exécution
     */
    public Object invoke(String methodName, Object... args) throws InvocationTargetException, IllegalAccessException {
        Method method = null;

        // On construit la signature des arguments
        String argTypes = String.join(", ", Arrays
            .stream( args )
            .map( o -> o.getClass().getCanonicalName() )
            .collect( Collectors.toList() )
        );

        // On recherche toutes les méthodes publiques correspondantes
        for(Map.Entry<String, Method> entry : this.currentMethodMap.entrySet())
        {
            Method m = entry.getValue();
            String k = entry.getKey();
            String argKey = methodName + args.length + argTypes;

            if(k.equals(argKey) && Modifier.isPublic(m.getModifiers()))
            {
                method = m;
                break;
            }
        }

        // Si on a pas trouvé la méthode
        if(method == null)
        {
            String ex = "The method "
                + this.currentClass.getCanonicalName() +"::"+ methodName +"("+ argTypes +") "
                + "doesn't exists"
            ;

            throw new IllegalArgumentException(ex);
        }

        return method.invoke(
            Modifier.isStatic(method.getModifiers()) ? null : this.currentObject,
            args
        );
    }

    /**
     * Récupère la valeur d'un champ depuis l'objet courant.
     * @param fieldName Le nom du champ à retrouver
     * @return La valeur
     */
    public Object getFieldValue(String fieldName)
    {
        if(!this.currentFieldMap.containsKey(fieldName))
        {
            return null;
        }

        return Reflector.getFieldValue(this.currentFieldMap.get(fieldName), this.currentObject);
    }

    /**
     * Retourne une clé depuis une instance d'Executable
     * @param exec L'objet à traiter
     * @return La clé sous forme de tableau
     */
    private String getExecutableKey(Executable exec)
    {
        Collection<String> types = Arrays
            .stream( exec.getParameterTypes() )
            .map( Class::getCanonicalName )
            .collect( Collectors.toList() )
        ;

        return exec.getName() + exec.getParameterCount() + String.join(", ", types);
    }

    /**
     * Getter de l'objet courant.
     * @return L'objet courant
     */
    public Object getCurrentObject() {
        return currentObject;
    }

    /**
     * Getter du package de l'objet courant.
     * @return Le package
     */
    public Package getCurrentPackage() {
        return currentPackage;
    }

    /**
     * Getter de la classe de l'objet courant.
     * @return La classe
     */
    public Class<?> getCurrentClass() {
        return currentClass;
    }

    /**
     * Getter de la map des classes héritées de l'objet courant.
     * @return La map
     */
    public HashMap<String, Class<?>> getCurrentClassMap() {
        return currentClassMap;
    }

    /**
     * Getter de la map des interfaces implémentées de l'objet courant.
     * @return La map
     */
    public HashMap<String, Type> getCurrentInterfaceMap() {
        return currentInterfaceMap;
    }

    /**
     * Getter de la map des constructeurs déclarés de l'objet courant.
     * @return La map
     */
    public HashMap<String, Constructor<?>> getCurrentConstructorMap() {
        return currentConstructorMap;
    }

    /**
     * Getter de la map des méthodes déclarées de l'objet courant.
     * @return La map
     */
    public HashMap<String, Method> getCurrentMethodMap() {
        return currentMethodMap;
    }

    /**
     * Getter de la map des champs déclarés de l'objet courant.
     * @return La map
     */
    public HashMap<String, Field> getCurrentFieldMap() {
        return currentFieldMap;
    }

    /**
     * @return Le nom de classe de l'objet courant
     */
    public String toString()
    {
        return this.serializer.toStack(this);
    }
}
