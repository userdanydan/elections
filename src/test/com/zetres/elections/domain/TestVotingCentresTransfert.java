package com.zetres.elections.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestVotingCentresTransfert {

    @Test
    void testMove(){
        var expected = "Move";
        var actual = VotingTransfertType.MOVE.getTypeName();
        assertEquals(expected, actual);
    }
}
