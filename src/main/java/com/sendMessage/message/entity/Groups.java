package com.sendMessage.message.entity;

import java.util.Date;

public class Groups {
        private String creator;
        private Date createTime;
        private String name;
        private String _id;
        private String avatar;
        public void setCreator(String creator) {
            this.creator = creator;
        }
        public String getCreator() {
            return creator;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
        public Date getCreateTime() {
            return createTime;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
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
}
