package xyz.jdynb.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 实现时间的自动填充处理
 */
@Component
@Slf4j
public class DateTimeMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
      this.strictInsertFill(metaObject, "create_time", LocalDateTime.class, LocalDateTime.now());
      this.strictInsertFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
    }
}
