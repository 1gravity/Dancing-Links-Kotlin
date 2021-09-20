package com.onegravity.dlx.model

import com.onegravity.dlx.getDLX
import com.onegravity.dlx.matrixTest1
import com.onegravity.dlx.matrixTest2
import com.onegravity.dlx.matrixTest3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RootNodeTests {

    @Test
    fun testGetHeaders() {
        assertEquals(7, matrixTest1.getDLX().getHeaders().size)
        assertEquals(12, matrixTest2.getDLX().getHeaders().size)
        assertEquals(12, matrixTest3.getDLX().getHeaders().size)
    }

}
