package com.zhou.movies.view.state;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.view.MovieView;

public interface FormState {
    FormState handleSubmit(MovieView context, MovieDTO dto);
    void enterState(MovieView context);
}
