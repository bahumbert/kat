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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * L'interface permettant de sérialiser un Reflector
 */
public interface ReflectorSerializer {

    /**
     * Transforme les données extraites de l'objet courant en une stack lisible.
     *
     * @param reflector Le Reflector à sérialiser
     * @return La stack
     */
    String toStack(Reflector reflector);

    /**
     * Retourne la réprésentation du paramètre passé en paramètre.
     *
     * @param param Le paramètre à traduire
     * @return La chaine de caractères correspondante
     */
    String serializeParameter(Parameter param);

    /**
     * Retourne la réprésentation de la méthode passée en paramètre.
     *
     * @param method La méthode à traduire
     * @return La chaine de caractères correspondante
     */
    String serializeMethod(Method method);

    /**
     * Retourne la réprésentation du champ passé en paramètre.
     *
     * @param field Le champ à traduire
     * @param object L'objet qui va fournir la valeur
     * @return La chaine de caractères correspondante
     */
    String serializeField(Field field, Object object);

    /**
     * Retourne la réprésentation de la valeur passée en paramètre.
     *
     * @param value La valeur à traduire
     * @return La chaine de caractères correspondante
     */
    String serializeValue(Object value);

    /**
     * Retourne la réprésentation des modifiers passés en paramètre.
     *
     * @param modifiers La liste des modifiers
     * @return La représentation des modifiers
     */
    String serializeModifiers(int modifiers);
}
