package io.github.sashirestela.openai.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.AbstractList;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class Page<T> extends AbstractList<T> {

    private String object;
    private List<T> data;
    private String firstId;
    private String lastId;
    private boolean hasMore;

    @Override
    public T get(int index) {
        return data.get(index);
    }

    @Override
    public int size() {
        return data.size();
    }

    public T first() {
        return get(0);
    }

    public T last() {
        return get(size() - 1);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

}
