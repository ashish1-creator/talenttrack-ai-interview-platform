package com.talenttrack.model;

    public class InterviewRequest {
        private String role;
        private String level;

        public InterviewRequest() {
        }

        public InterviewRequest(String role, String level) {
            this.role = role;
            this.level = level;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }