package common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder//创建者模式又叫建造者模式。简单来说，就是一步步创建一个对象，
// 它对用户屏蔽了里面构建的细节，但却可以精细地控制对象的构造过程。
@Data//注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    // 客户端和服务端共有的
    private Integer id;
    private String userName;
    private Boolean sex;
}
