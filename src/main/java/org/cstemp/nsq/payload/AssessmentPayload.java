/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;

import org.cstemp.nsq.util.XCellDetail;
import lombok.Data;

/**
 *
 * @author chibuezeharry
 */
public class AssessmentPayload {

    @Data
    public static class AssessmentUnit {

        @XCellDetail(indexed = true, label = "Index")
        private Integer unitIndex;

        @XCellDetail(indexed = true)
        private String title;

        @XCellDetail(indexed = true, label = "Unit Reference")
        private String unitRef;

        @XCellDetail(indexed = true)
        private Integer credit;

        @XCellDetail(indexed = true, label = "Guided Learning Hours")
        private Integer glh;

        @XCellDetail(indexed = true)
        private Integer course;

        private String remarks;

        @XCellDetail(indexed = true, label = "Total Credit")
        private Integer totalCredit;

        @XCellDetail(indexed = true)
        private String requirements;

        @XCellDetail(indexed = true)
        private String objectives;

    }

    @Data
    public static class LearningOutcome {

    }

    @Data
    public static class PerformanceCriteria {

    }

}
