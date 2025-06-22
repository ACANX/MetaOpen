package com.acanx.meta.model.test.object.copier;

import com.acanx.meta.model.test.annotation.model.MessageFlex;
import com.acanx.meta.model.test.annotation.model.MessageStable;
import com.acanx.meta.model.test.object.copy.ObjectCopierProcessorCopier;

/**
 * MessageStableCopier
 *
 * @author ACANX
 * @since 202506
 */
public class MessageCopier {

    /**
     *   转换
     *
     *  {@link ObjectCopierProcessorCopier#convertMessageFlexToMessageStable(MessageFlex, MessageStable)}
     *
     * @param source  源
     * @param target  目标
     */
    public static void convertMessageFlexToMessageStable(MessageFlex source, MessageStable target) {
        if (source != null) {
            if (source.getRemark() != null) {
                target.setRemark(source.getRemark());
            }
            if (source.getId() != null) {
                target.setId(source.getId());
            }
        }

    }


}
