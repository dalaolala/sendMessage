package com.sendMessage.message.entity;

import java.util.List;

public class User {
        private List<Groups> groups;
        private String _id;
        private String avatar;
        private String tag;
        private boolean isAdmin;
        private List<String> friends;
        private String username;
        private String token;
        public void setGroups(List<Groups> groups) {
            this.groups = groups;
        }
        public List<Groups> getGroups() {
            return groups;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
        public String get_id() {
            return _id;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public String getAvatar() {
            return avatar;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
        public String getTag() {
            return tag;
        }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }
        public boolean getIsAdmin() {
            return isAdmin;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }
        public List<String> getFriends() {
            return friends;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }

        public void setToken(String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }

    }
