package com.zetres.elections.domain;

public enum VotingTransfertType {
    MOVE("Move");
    private String typeName;
    VotingTransfertType(String typeName){
        this.typeName=typeName;
    }
    public String getTypeName(){
        return this.typeName;
    }

}
