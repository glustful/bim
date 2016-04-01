package com.atide.bim.request;

public enum PartWebServiceValue{
        GetUserProjects("111","111");

        private String url,methodName;
        private PartWebServiceValue(String url,String methodName){
            this.url = url;
            this.methodName = methodName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

    }