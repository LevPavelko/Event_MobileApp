package com.example.event_app.data;

import java.io.Serializable;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event  implements Serializable {
    private Integer id;
    private String name;
    private String date;
    private String phone;
   
    private String place;


}

