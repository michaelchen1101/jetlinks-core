package org.jetlinks.core.metadata.types;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;


public class DateTimeTypeTest {

    @Test
    public void test(){
        DateTimeType timeType=new DateTimeType();
        timeType.setZoneId(ZoneId.of("Asia/Shanghai"));
        Assert.assertTrue(timeType.validate(System.currentTimeMillis()).isSuccess());


        Assert.assertNotNull(timeType.format(System.currentTimeMillis()));

        timeType.setFormat("yyyy-MM-dd");

        Assert.assertEquals(timeType.format(Date.from(LocalDateTime.of(2019,8,10,0,0)
                        .toInstant(ZoneOffset.of("+8")))),
                "2019-08-10");


    }

}