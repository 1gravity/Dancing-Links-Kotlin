package com.onegravity.dlx.model

import com.onegravity.dlx.toDLX
import com.onegravity.dlx.matrixTest1
import com.onegravity.dlx.matrixTest2
import com.onegravity.dlx.matrixTest3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RootNodeTests {

    @Test
    fun testGetHeaders() {
        assertEquals(7, matrixTest1.toDLX().getHeaders().size)
        assertEquals(12, matrixTest2.toDLX().getHeaders().size)
        assertEquals(12, matrixTest3.toDLX().getHeaders().size)
    }

}
