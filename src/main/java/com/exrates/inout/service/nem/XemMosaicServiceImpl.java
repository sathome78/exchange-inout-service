package com.exrates.inout.service.nem;

//exrates.model.dto.MosaicIdDto;

import com.exrates.inout.domain.dto.MosaicIdDto;
import com.exrates.inout.properties.models.XemProperty;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.primitive.Quantity;
import org.nem.core.model.primitive.Supply;

/**
 * Created by Maks on 27.02.2018.
 */
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
    public Quantity getLevyFee() {
        return levyFee;
    }

    @Override
    public MosaicId mosaicId() {
        return mosaicId;
    }

    @Override
    public Supply getSupply() {
        return supply;
    }

    @Override
    public int getDivisibility() {
        return divisibility;
    }

    @Override
    public MosaicIdDto getMosaicId() {
        return mosaicIdDto;
    }

    @Override
    public String getMerchantName() {
        return merchantName;
    }

    @Override
    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public long getDecimals() {
        return decimals;
    }
}
