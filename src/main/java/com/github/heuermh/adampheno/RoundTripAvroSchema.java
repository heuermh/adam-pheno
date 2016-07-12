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

import java.io.File;

import org.apache.avro.Schema;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.CodecFactory;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DatumReader;

import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.reflect.ReflectDatumReader;

import org.phenopackets.api.PhenoPacket;

/**
 * Round trip avro schema generated reflectively from the PhenoPacket class.
 *
 * @author  Michael Heuer
 */
public final class RoundTripAvroSchema {

    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) throws Exception {
        File file = File.createTempFile("RoundTripAvroSchema", "avro");

        Schema.Parser parser = new Schema.Parser();
        parser.setValidate(true);
        Schema schema = parser.parse(System.in);

        // create a file of packets
        DatumWriter<PhenoPacket> writer = new ReflectDatumWriter<PhenoPacket>(PhenoPacket.class);
        DataFileWriter<PhenoPacket> out = new DataFileWriter<PhenoPacket>(writer)
            .setCodec(CodecFactory.deflateCodec(9))
            .create(schema, file);

        PhenoPacket.Builder builder = PhenoPacket.newBuilder()
            .title("title")
            .context("context");

        // write 100 packets to the file
        for (int i = 0; i < 100; i++) {
            out.append(builder.id("id" + i).build());
        }

        // close the output file
        out.close();

        // open a file of packets
        DatumReader<PhenoPacket> reader = new ReflectDatumReader<PhenoPacket>(PhenoPacket.class);
        DataFileReader<PhenoPacket> in = new DataFileReader<PhenoPacket>(file, reader);

        // read 100 packets from the file & print ids to stdout
        for (PhenoPacket packet : in) {
            System.out.println(packet.getId());
        }

        // close the input file
        in.close();
    }
}
