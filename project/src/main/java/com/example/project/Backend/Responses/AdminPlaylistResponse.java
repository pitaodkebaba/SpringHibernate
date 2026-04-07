package com.example.project.Backend.Responses;

import com.example.project.Backend.Models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@Data
public class AdminPlaylistResponse extends PlaylistResponse{
    private int id;
    private int ownerId;
    private User.Role ownerRole;
}
