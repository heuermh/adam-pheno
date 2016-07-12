/**
 * Copyright 2016 held jointly by the individual authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.heuermh.adampheno;

import org.apache.avro.Schema;

/**
 * Validate avro schema generated reflectively from the PhenoPacket class.
 *
 * @author  Michael Heuer
 */
public final class ValidateAvroSchema {

    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) throws Exception {
        Schema.Parser parser = new Schema.Parser();
        parser.setValidate(true);
        parser.parse(System.in);
    }
}
