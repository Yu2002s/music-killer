package xyz.jdynb.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.experimental.Accessors;

import java.util.Date;

import lombok.Data;

@Data
@Accessors(chain = true)
public class Album {

    @JsonAlias("albuminfo")
    private String albumInfo;

    private String artist;

    private String releaseDate;

    private String album;

    @JsonAlias("albumid")
    private Long albumId;

    @JsonAlias("artistid")
    private Long artistId;

    private String pic;

    private String lang;

}