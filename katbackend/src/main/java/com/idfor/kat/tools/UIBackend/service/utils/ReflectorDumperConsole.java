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
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Singleton dédié à l'introspection d'objets.
 */
public class ReflectorDumperConsole implements ReflectorSerializer
{
    private Comparator<Field> comparator;
    private Predicate<Field> predicate;

    private static final Set<Class> WRAPPER_TYPES = new HashSet(Arrays.asList(
        Boolean.class, Character.class, Byte.class, Short.class,
        Integer.class, Long.class, Float.class, Double.class
    ));

    /**
     * Le constructeur du serializer
     */
    public ReflectorDumperConsole()
    {
        this.comparator = new Comparator<Field>()
        {
            @Override
            public int compare(Field m1, Field m2)
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

        this.predicate = new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                int modifiers = field.getModifiers();

                return (
                    Modifier.isPublic( modifiers )
                    || Modifier.isProtected( modifiers )
                    || Modifier.isPrivate( modifiers )
                );
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
        StringBuilder stack = new StringBuilder()
            .append( eol )
            .append(eol)
            .append(reflector.getCurrentClass().getCanonicalName())
            .append("@")
            .append(Integer.toHexString(reflector.getCurrentObject().hashCode()))
            .append(eol)
        ;

        // On filtre les champs pour avoir que les publics
        List<Field> fields = reflector.getCurrentFieldMap().values()
            .stream()
            .filter(this.predicate)
            .sorted(this.comparator)
            .collect(Collectors.toList())
        ;

        for(Field f : fields)
        {
            String toPrint = this.serializeField(f, reflector.getCurrentObject());

            if(toPrint.length() == 0) continue;

            stack
                .append( "    " )
                .append( toPrint )
                .append( eol )
            ;
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
        StringBuilder stack = new StringBuilder();

        if(value == null)
        {
            stack.append( "null" );
        }
        else if(value.getClass().isArray())
        {
            Iterator objIterator = Arrays.asList(value).iterator();
            Object next;

            while(objIterator.hasNext())
            {
                next = objIterator.next();

                // TODO : A compléter plus tard
            }
        }
        else if(value instanceof Iterable)
        {
            Iterator objIterator = ((Iterable) value).iterator();
            Object next;

            while(objIterator.hasNext())
            {
                next = objIterator.next();

                // TODO : A compléter plus tard
            }
        }
        else if(WRAPPER_TYPES.contains( value.getClass() ))
        {
            stack.append( String.valueOf( value ) );
        }
        else
        {
            stack.append( value.toString() );
        }

        return stack.toString().trim();
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

        if(value == null)
        {
            return "";
        }

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
            .append( " = " )
            .append( this.serializeValue(value) )
        ;

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
        return "";
    }

    /**
     * Retourne la réprésentation du paramètre passé en paramètre.
     *
     * @param param Le paramètre à traduire
     * @return La chaine de caractères correspondante
     */
    public String serializeParameter(Parameter param)
    {
        return "";
    }
}
