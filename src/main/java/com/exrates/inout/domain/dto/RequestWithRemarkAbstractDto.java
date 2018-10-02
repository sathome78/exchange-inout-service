package com.exrates.inout.domain.dto;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public abstract class RequestWithRemarkAbstractDto {
    private String remark;

    public void setRemark(String remark, String prefix) {
        if (StringUtils.isEmpty(remark)) {
            return;
        }
        String currentRemark = this.remark == null ? "" : this.remark;
        String addPhrase = (StringUtils.isEmpty(prefix) ? "" : "\n".concat(prefix))
                .concat(remark);
        this.remark = currentRemark.concat(addPhrase);
    }

}
