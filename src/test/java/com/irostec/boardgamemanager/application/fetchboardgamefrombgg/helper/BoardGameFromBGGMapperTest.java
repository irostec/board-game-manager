package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.common.exception.BGMException;

import com.irostec.boardgamemanager.TestingUtils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardGameFromBGGMapperTest {

    @Test
    void roundtrip() throws BGMException {

        final BoardGameFromBGG source = TestingUtils.buildBoardGameFromBGG();

        final BoardGameFromBGG result = BoardGameFromBGGMapper.INSTANCE.toBoardGameFromBGG(
                BoardGameFromBGGMapper.INSTANCE.toBoardGameFromBGGWithPartitionedLinks(source)
        );

        assertEquals(source, result);
    }

}
