package cn.z201.mybatis.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author z201.coding@gmail.com
 **/
@Setter
@Getter
public class ExplainResultDto {

    private String id;
    private String selectType;
    private String table;
    private String partitions;
    private String type;
    private String possibleKeys;
    private String key;
    private String keyLen;
    private String ref;
    private String rows;
    private String filtered;
    private String extra;

}
