package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.dto.response.HomePageResponseForAdmin;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface HomePageService {

    /**
     * fetch homepage for user.
     *
     * @param userId
     * @return
     */
    public HomePageResponse fetchHomePage(Long userId);


    /**
     * fetch homepage for admin.
     *
     * @param states
     * @param start
     * @param end
     * @param areaIds
     * @param taskType
     * @param orderId
     * @param patientFilterValue
     * @param pageable
     * @return
     */
    public HomePageResponseForAdmin fetchHomePageForAdmin(TaskState[] states,
                                                          Timestamp start, Timestamp end,
                                                          List<Long> areaIds, TaskType taskType, Long orderId,
                                                          String patientFilterValue,
                                                          Long providerId,
                                                          Pageable pageable);

    /**
     * set count for admin homepage response body.
     * @param adminResponse
     * @param allTasks
     * @param startedTasks
     * @param acceptedTasks
     * @param rejectedTasks
     * @param rescheduledTasks
     * @param openTasks
     * @param pendingTasks
     * @param cancelledTasks
     * @param completedTasks
     * @return
     */
    public HomePageResponseForAdmin setCount(HomePageResponseForAdmin adminResponse, PaginatedResponse allTasks, PaginatedResponse startedTasks, PaginatedResponse acceptedTasks, PaginatedResponse rejectedTasks, PaginatedResponse rescheduledTasks,
                                             PaginatedResponse openTasks, PaginatedResponse pendingTasks,
                                             PaginatedResponse cancelledTasks,
                                             PaginatedResponse completedTasks);
    }
