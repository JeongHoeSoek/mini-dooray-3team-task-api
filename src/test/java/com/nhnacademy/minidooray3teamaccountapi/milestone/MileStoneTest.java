package com.nhnacademy.minidooray3teamaccountapi.milestone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import org.junit.jupiter.api.Test;

class MileStoneTest {

    @Test
    void testGettersAndSetters() {
        MileStone mileStone = new MileStone();
        mileStone.setMilestoneId(1L);
        mileStone.setName("Test Milestone");
        mileStone.setStatus(MileStone.Status.START);

        assertEquals(1L, mileStone.getMilestoneId());
        assertEquals("Test Milestone", mileStone.getName());
        assertEquals(MileStone.Status.START, mileStone.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        MileStone mileStone = new MileStone(1L, "Test Milestone", MileStone.Status.START, null, null);

        assertEquals(1L, mileStone.getMilestoneId());
        assertEquals("Test Milestone", mileStone.getName());
        assertEquals(MileStone.Status.START, mileStone.getStatus());
    }

    @Test
    void testPatch() {
        MileStone mileStone = new MileStone();
        mileStone.setName("Original Name");
        mileStone.setStatus(MileStone.Status.PROGRESS);

        MileStone updatedMileStone = new MileStone();
        updatedMileStone.setName("Updated Name");
        updatedMileStone.setStatus(MileStone.Status.END);

        mileStone.patch(updatedMileStone);

        assertEquals("Updated Name", mileStone.getName());
        assertEquals(MileStone.Status.END, mileStone.getStatus());
    }
}
