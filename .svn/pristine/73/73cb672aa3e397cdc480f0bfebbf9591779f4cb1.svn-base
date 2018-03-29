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

import java.lang.reflect.*;
import java.util.*;

/**
 * Singleton dédié à l'introspection d'objets.
 */
public class ReflectorSerialiserConsole implements ReflectorSerializer
{
    private Comparator<Member> comparator;

    /**
     * Le constructeur du serializer
     */
    public ReflectorSerialiserConsole()
    {
        this.comparator = new Comparator<Member>()
        {
            @Override
            public int compare(Member m1, Member m2)
            {
                if(m1.getModifiers() > m2.getModifiers())
                {
                    return 1;
                }
                else if(m1.getModifiers() < m2.getModifiers())
                {
                    return -1;
                }
                else
                {
                    return m1.getName().compareToIgnoreCase( m2.getName() );
                }
            }
        };
    }

    /**
     * Transforme les données extraites de l'objet courant en une stack lisible.
     *
     * @param reflector Le Reflector à sérialiser
     * @return La stack
     */
    public String toStack(Reflector reflector)
    {
        String eol = "\n";
        StringBuilder stack = new StringBuilder();

        List<Field> fields = new ArrayList<>( reflector.getCurrentFieldMap().values() );
        List<Method> methods = new ArrayList<>( reflector.getCurrentMethodMap().values() );

        Collections.sort(fields, this.comparator);
        Collections.sort(methods, this.comparator);

        stack
            .append(eol)
            .append(eol)
            .append(reflector.getCurrentClass().getCanonicalName())
            .append("@")
            .append(Integer.toHexString(reflector.getCurrentObject().hashCode()))
            .append(eol)
            .append(eol)
            .append("  > Value")
            .append(eol)
            .append(eol)
            .append("    ")
            .append(this.serializeValue(reflector.getCurrentObject()))
            .append(eol)
        ;

        if(!fields.isEmpty())
        {
            stack
                .append( eol )
                .append( "  > Fields" )
                .append( eol )
                .append( eol )
            ;

            for(Field f : fields)
            {
                stack
                    .append( "    " )
                    .append( this.serializeField(f, reflector.getCurrentObject()) )
                    .append( eol )
                ;
            }
        }

        if(!methods.isEmpty())
        {
            stack
                .append( eol )
                .append( "  > Methods" )
                .append( eol )
                .append( eol )
            ;

            for(Method m : methods)
            {
                stack
                    .append( "    " )
                    .append( this.serializeMethod(m) )
                    .append( eol )
                ;
            }
        }

        return stack.toString();
    }

    /**
     * Retourne la réprésentation des modifiers passés en paramètre.
     *
     * @param modifiers La liste des modifiers
     * @return La représentation des modifiers
     */
    public String serializeModifiers(int modifiers)
    {
        ArrayList<String> list = new ArrayList<>();

        if(Modifier.isPublic( modifiers ))
        {
            list.add("+");
        }
        else if(Modifier.isPrivate( modifiers ))
        {
            list.add("-");
        }
        else if(Modifier.isProtected( modifiers ))
        {
            list.add("#");
        }

        if(Modifier.isAbstract( modifiers ))
        {
            list.add("[A]");
        }

        if(Modifier.isFinal( modifiers ))
        {
            list.add("[F]");
        }

        if(Modifier.isStatic( modifiers ))
        {
            list.add("[S]");
        }

        if(Modifier.isNative( modifiers ))
        {
            list.add("[N]");
        }

        if(Modifier.isTransient( modifiers ))
        {
            list.add("[T]");
        }

        return String.join(" ", list);
    }

    /**
     * Retourne la réprésentation de la valeur passée en paramètre.
     *
     * @param value La valeur à traduire
     * @return La chaine de caractères correspondante
     */
    public String serializeValue(Object value)
    {
        return value == null ? "null" : value.toString().trim();
    }

    /**
     * Retourne la réprésentation du champ passé en paramètre.
     *
     * @param field Le champ à traduire
     * @return La chaine de caractères correspondante
     */
    public String serializeField(Field field, Object object)
    {
        StringBuilder stack = new StringBuilder();
        String modifiers = this.serializeModifiers( field.getModifiers() );
        Object value = Reflector.getFieldValue(field, object);

        if(modifiers.length() > 0)
        {
            stack
                .append( modifiers )
                .append( " " )
            ;
        }

        stack
            .append( field.getName() )
            .append( ": " )
            .append( field.getGenericType().getTypeName() )
        ;

        if(value != null)
        {
            stack
                .append( " = " )
                .append( this.serializeValue(value) )
            ;
        }

        return stack.toString();
    }

    /**
     * Retourne la réprésentation de la méthode passée en paramètre.
     *
     * @param method La méthode à traduire
     * @return La chaine de caractères correspondante
     */
    public String serializeMethod(Method method)
    {
        StringBuilder stack = new StringBuilder();

        String modifiers = this.serializeModifiers( method.getModifiers() );
        ArrayList<String> params = new ArrayList<>();
        ArrayList<String> exceptions = new ArrayList<>();

        for(Parameter p : method.getParameters())
        {
            params.add( this.serializeParameter(p) );
        }

        for(Class<?> cls : method.getExceptionTypes())
        {
            exceptions.add( cls.getCanonicalName() );
        }

        if(modifiers.length() > 0)
        {
            stack
                .append( modifiers )
                .append( " " )
            ;
        }

        stack
            .append( method.getName() )
            .append( "(" )
            .append( String.join(", ", params) )
            .append( ")" )
            .append( ": " )
            .append( method.getGenericReturnType().getTypeName() )
        ;

        if(!exceptions.isEmpty())
        {
            stack
                .append( " -> throws " )
                .append( String.join(", ", exceptions) )
            ;
        }

        return stack.toString();
    }

    /**
     * Retourne la réprésentation du paramètre passé en paramètre.
     *
     * @param param Le paramètre à traduire
     * @return La chaine de caractères correspondante
     */
    public String serializeParameter(Parameter param)
    {
        StringBuilder stack = new StringBuilder();

        String modifiers = this.serializeModifiers( param.getModifiers() );

        if(modifiers.length() > 0)
        {
            stack
                .append( modifiers )
                .append( " " )
            ;
        }

        stack
            .append( param.getType().getCanonicalName() )
        ;

        return stack.toString();
    }
}
