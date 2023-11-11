package com.principes.rightchain.chain.entity;

/**
 * REPORT_SUBMITTED : 신고 접수
 * CASE_UNDER_REVIEW : 신고 사안 심의
 * REVIEW_RESULT_REPORTED : 심의 결과 보고
 * FINAL_JUDGMENT : 교권침해 사안 최종 판결
 * CASE_CLOSED : 사건 종결
 */
public enum ProgressStatus {
    REPORT_SUBMITTED,
    CASE_UNDER_REVIEW,
    REVIEW_RESULT_REPORTED,
    FINAL_JUDGMENT,
    CASE_CLOSED;

    public static String getProgressStatusDescription(ProgressStatus status) {
        return switch (status) {
            case REPORT_SUBMITTED -> "신고_접수";
            case CASE_UNDER_REVIEW -> "신고_사안_심의";
            case REVIEW_RESULT_REPORTED -> "심의_결과_보고";
            case FINAL_JUDGMENT -> "교권침해_사안_최종_판결";
            case CASE_CLOSED -> "사건_종결";
        };
    }
}
