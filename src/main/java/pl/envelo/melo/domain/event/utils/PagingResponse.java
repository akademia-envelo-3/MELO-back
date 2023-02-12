package pl.envelo.melo.domain.event.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.envelo.melo.domain.event.Event;

import java.util.List;

/**
 * DTO using for pagination
 */
@Getter
@Setter
@AllArgsConstructor
public class PagingResponse {

    /**
     * entity count
     */
    private Long count;
    /**
     * page number, 0 indicate the first page.
     */
    private Long pageNumber;
    /**
     * size of page, 0 indicate infinite-sized.
     */
    private Long pageSize;
    /**
     * Offset from the of pagination.
     */
    private Long pageOffset;
    /**
     * the number total of pages.
     */
    private Long pageTotal;
    /**
     * elements of page.
     */
    private List<Event> elements;
}
