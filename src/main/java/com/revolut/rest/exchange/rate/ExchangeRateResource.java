package com.revolut.rest.exchange.rate;

import com.revolut.MoneyFormatUtil;
import com.revolut.dao.exchange.rate.ExchangeRateDao;
import com.revolut.model.exchange.rate.ExchangeRateRequest;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/exchangerate")
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeRateResource {

	private final ExchangeRateDao dao;

	public ExchangeRateResource(ExchangeRateDao dao) {
		this.dao = dao;
	}

	@POST
	@UnitOfWork
	public Response addExchangeRate(ExchangeRateRequest request) {

		BigDecimal rate = MoneyFormatUtil.parse(request.getRate(), 5);

		return Response.ok(
				dao.save(request.getFromCurrency(), request.getToCurrency(), rate)
		).build();
	}

}
