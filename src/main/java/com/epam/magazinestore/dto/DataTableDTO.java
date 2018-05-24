package com.epam.magazinestore.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataTableDTO<T> {

  private List<T> data;
  private long recordsTotal;
  private long recordsFiltered;
  private int draw;
}
