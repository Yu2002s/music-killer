package xyz.jdynb.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.experimental.Accessors;

import java.util.Date;

import lombok.Data;

@Data
@Accessors(chain = true)
public class MusicRank {

    private String leader;

    private Long num;

    private String name;

    @JsonAlias({"img", "pic"})
    private String pic;

    private Long id;

    private String pub;

    private List<Music> musicList;
}