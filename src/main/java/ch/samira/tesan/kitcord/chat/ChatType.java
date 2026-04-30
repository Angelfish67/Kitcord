package ch.samira.tesan.kitcord.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


public enum ChatType {
    DIRECT,
    GROUP,
}