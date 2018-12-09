package com.exrates.inout.service.nem;

import com.exrates.inout.domain.dto.MosaicIdDto;
import com.exrates.inout.properties.models.XemProperty;
import lombok.Getter;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.primitive.Quantity;
import org.nem.core.model.primitive.Supply;

@Getter
public class XemMosaicServiceImpl implements XemMosaicService {

    private String merchantName;
    private String currencyName;
    private MosaicIdDto mosaicIdDto;
    private long decimals;
    private int divisibility;
    private Supply supply;
    private MosaicId mosaicId;
    private Quantity levyFee;


    public XemMosaicServiceImpl(XemProperty property) {
        this.merchantName = property.getMerchantName();
        this.currencyName = property.getCurrencyName();
        this.mosaicIdDto = new MosaicIdDto(property.getNameSpaceId(), property.getName());
        this.decimals = property.getDecimals();
        this.divisibility = property.getDivisibility();
        this.supply = new Supply(property.getSupply());
        this.mosaicId = new MosaicId(new NamespaceId(mosaicIdDto.getNamespaceId()), mosaicIdDto.getName());
        this.levyFee = new Quantity(property.getLevyFee());
    }

    @Override
    public MosaicId mosaicId() {
        return mosaicId;
    }
}
