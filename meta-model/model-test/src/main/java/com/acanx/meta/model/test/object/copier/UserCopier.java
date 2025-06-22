package com.acanx.meta.model.test.object.copier;

import com.acanx.meta.model.test.json.model.User;
import com.acanx.meta.model.test.json.model.UserDTO;
import com.acanx.meta.model.test.object.copy.ObjectCopierProcessorCopier;

/**
 * UserCopier
 *
 * @author ACANX
 * @since 202506
 */
public class UserCopier {


    /**
     *  convertUser
     *
     * {@link ObjectCopierProcessorCopier#convertUserToUserDTO(User, UserDTO)}
     *
     * @param source  源
     * @param target  目标
     */
    public static void convertUserToUserDTO(User source, UserDTO target) {
        if (source != null) {
            if (source.getPassword() != null) {
                target.setPassword(source.getPassword());
            }
            if (source.getCreateTime() != null) {
                target.setCreateTime(source.getCreateTime());
            }
            if (source.getUserId() != null) {
                target.setUserId(source.getUserId());
            }
        }
    }

    /**
     *   copyUser
     *
     *   {@link ObjectCopierProcessorCopier#convertUserToUserDTOWithIgnorePassword(User, UserDTO)}
     *
     * @param source  源
     * @param target  目标
     */
    public static void convertUserToUserDTOWithIgnorePassword(User source, UserDTO target) {
        if (source != null) {
            if (source.getCreateTime() != null) {
                target.setCreateTime(source.getCreateTime());
            }
            if (source.getUserId() != null) {
                target.setUserId(source.getUserId());
            }
            if (source.getUserName() != null) {
                target.setLoginId(source.getUserName());
            }
            if (source.getEmail() != null) {
                target.setContactEmail(source.getEmail());
            }
        }
    }
}
