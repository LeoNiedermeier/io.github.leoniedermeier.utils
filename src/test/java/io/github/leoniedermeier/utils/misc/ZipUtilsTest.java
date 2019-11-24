package io.github.leoniedermeier.utils.misc;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ZipUtilsTest {
   
    @Nested
    static class SingleContent {
        
        
        @Test
        void zipUnzip() throws Exception{
            byte[] bytes = RandomUtils.nextBytes(100);
            byte[] zip = ZipUtils.zipSingleContent("entryName", bytes);
            
            byte[] unzip = ZipUtils.unzipSingleContent(zip);
            assertArrayEquals(bytes, unzip);
        }
    }

}
