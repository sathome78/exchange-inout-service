package com.exrates.inout.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class NemMosaicTransferDto {

    private MosaicIdDto mosaicIdDto;
    private BigDecimal quantity;
    private Object service;

    public NemMosaicTransferDto(MosaicIdDto mosaicIdDto, BigDecimal quantity) {
        this.mosaicIdDto = mosaicIdDto;
        this.quantity = quantity;
    }

    public NemMosaicTransferDto() {
    }

    public NemMosaicTransferDto(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("mosaicId")
    private void unpackNested(Map<String,String> map) {
        String namespaceId = map.get("namespaceId");
        String name = map.get("name");
        this.mosaicIdDto = new MosaicIdDto(namespaceId, name);
    }
}
