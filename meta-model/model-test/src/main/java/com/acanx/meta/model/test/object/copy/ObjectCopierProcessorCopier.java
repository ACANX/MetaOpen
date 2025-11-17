package com.acanx.meta.model.test.object.copy;

import com.acanx.meta.model.test.annotation.model.MessageFlex;
import com.acanx.meta.model.test.annotation.model.MessageStable;
import com.acanx.meta.model.test.json.model.User;
import com.acanx.meta.model.test.json.model.UserDTO;

/**
 * Incubator-Annotation生成的对象拷贝Copier类
 *
 * 支持功能:
 * - 同名同类型字段自动复制
 * - 基本类型与包装类型兼容
 * - 常用类型自动转换
 * - 字段映射配置
 *
 * 方法列表:
 * - convertMessageFlexToMessageStable()
 * - convertUserToUserDTO()
 * - convertUserToUserDTOWithIgnorePassword()
 */
public final class ObjectCopierProcessorCopier {
  /**
   * Incubator-Annotation生成的拷贝方法
   * 用于替换 {@link CopierProcessor#convertMessageFlexToMessageStable(MessageFlex, MessageStable)}
   *
   * 配置信息:
   * - copyNulls: false
   * - 忽略字段: []
   *
   * @param source 源对象，包含待拷贝的数据
   * @param target 目标对象，接收拷贝后的数据
   */
  public static void convertMessageFlexToMessageStable(MessageFlex source, MessageStable target) {
    if (null != source && null != target) {
      if (null != source.getRemark()) {
        target.setRemark(source.getRemark());
      }
      if (null != source.getId()) {
        target.setId(source.getId());
      }
    }
  }

  /**
   * Incubator-Annotation生成的拷贝方法
   * 用于替换 {@link CopierProcessor#convertUserToUserDTO(User, UserDTO)}
   *
   * 配置信息:
   * - copyNulls: false
   * - 忽略字段: []
   *
   * @param source 源对象，包含待拷贝的数据
   * @param target 目标对象，接收拷贝后的数据
   */
  public static void convertUserToUserDTO(User source, UserDTO target) {
    if (null != source && null != target) {
      if (null != source.getPassword()) {
        target.setPassword(source.getPassword());
      }
      if (null != source.getCreateTime()) {
        target.setCreateTime(source.getCreateTime());
      }
      if (null != source.getUserId()) {
        target.setUserId(source.getUserId());
      }
    }
  }

  /**
   * Incubator-Annotation生成的拷贝方法
   * 用于替换 {@link CopierProcessor # convertUserToUserDTOWithIgnorePassword(User, UserDTO)}
   *
   * 配置信息:
   * - copyNulls: false
   * - 忽略字段: [password]
   * - 字段映射:
   *   - userName → loginId
   *   - email → contactEmail
   *
   * @param source 源对象，包含待拷贝的数据
   * @param target 目标对象，接收拷贝后的数据
   */
  public static void convertUserToUserDTOWithIgnorePassword(User source, UserDTO target) {
    if (null != source && null != target) {
      if (null != source.getCreateTime()) {
        target.setCreateTime(source.getCreateTime());
      }
      if (null != source.getUserId()) {
        target.setUserId(source.getUserId());
      }
      if (null != source.getUserName()) {
        target.setLoginId(source.getUserName());
      }
      if (null != source.getEmail()) {
        target.setContactEmail(source.getEmail());
      }
    }
  }
}
