package br.com.projeta.gestor.util;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import br.com.projeta.gestor.views.componentes.ItemCombo;

public class ItemComboConverter implements Converter<ItemCombo, Long> {

  @Override
  public Result<Long> convertToModel(ItemCombo value, ValueContext context) {
      if (value == null || value.value == null || value.value.equals("0")) {
          return Result.ok(null);
      }
      return Result.ok(Long.parseLong(value.value));
  }

  @Override
  public ItemCombo convertToPresentation(Long value, ValueContext context) {
      if (value == null) {
          return null;
      }
      return new ItemCombo(String.valueOf(value), "", null);
  }
}