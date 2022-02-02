package cn.z201.lucene;

import lombok.Data;

/**
 * @author z201.coding@gmail.com
 **/
@Data
public class ArticleEntity {

    private String id;

    private String title;

    private String content;

    private float score;
}
