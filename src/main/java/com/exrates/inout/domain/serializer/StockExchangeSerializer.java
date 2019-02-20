package com.exrates.inout.domain.serializer;

import com.exrates.inout.domain.dto.StockExchange;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by OLEG on 22.12.2016.
 */
public class StockExchangeSerializer extends JsonSerializer<StockExchange> {

    @Override
    public void serialize(StockExchange value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(value.getName());
    }
}
