package cn.z201.audit.domain.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiParam {
    private Long id;
}
