package io.github.sashirestela.openai.domain.chat.message;

import io.github.sashirestela.openai.domain.chat.Role;

public abstract class ChatMsg {

    protected Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
