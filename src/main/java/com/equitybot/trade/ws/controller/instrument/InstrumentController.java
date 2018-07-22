package com.equitybot.trade.ws.controller.instrument;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.equitybot.trade.db.mongodb.instrument.repository.InstrumentRepository;
import com.equitybot.trade.util.DateFormatUtil;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.mongodb.client.result.DeleteResult;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

@RestController
@RequestMapping("/api")
public class InstrumentController {
	@Autowired
	InstrumentRepository instrumentRepository;
	@Autowired
	private KiteConnectService tradePortZerodhaConnect;

	@GetMapping("/instrument/v1.0/{instrumentToken}")
	public InstrumentModel findByInstrumentTokenInDB(@PathVariable("instrumentToken") String instrumentToken) {
		return instrumentRepository.findByInstrumentToken(instrumentToken);
	}

	@GetMapping("/instrument/v1.0/exchangeToken/{exchangeToken}")
	public List<InstrumentModel> findByExchangeTokenToDB(@PathVariable("exchangeToken") String exchangeToken) {
		return instrumentRepository.findByExchangeToken(exchangeToken);
	}
	
	@GetMapping("/instrument/v1.0/tradingSymbol/{tradingSymbol}")
	public List<InstrumentModel> findByTradingSymbolToDB(@PathVariable("tradingSymbol") String tradingSymbol) {
		return instrumentRepository.findByTradingSymbol(tradingSymbol);
	}

	@GetMapping("/instrument/v1.0/nameRegX/{nameRegX}")
	public List<InstrumentModel> findByNameLikeDB(@PathVariable("nameRegX") String nameRegX) {
		return instrumentRepository.findByNameLike(nameRegX);
	}

	@GetMapping("/instrument/v1.0/instrumentType/{instrumentType}")
	public List<InstrumentModel> findByInstrumentTypeDB(@PathVariable("instrumentType") String instrumentType) {
		return instrumentRepository.findByInstrumentType(instrumentType);
	}

	@GetMapping("/instrument/v1.0/name/{name}")
	public List<InstrumentModel> findByNameDB(@PathVariable("name") String name) {
		return instrumentRepository.findByName(name);
	}

	@GetMapping("/instrument/v1.0/segment/{segment}")
	public List<Long> findBySegmentDB(@PathVariable("segment") String segment) {
		return instrumentRepository.findBySegment(segment);
	}

	@GetMapping("/instrument/v1.0/exchange/{exchange}")
	public List<InstrumentModel> findByExchangeDB(@PathVariable("exchange") String exchange) {
		return instrumentRepository.findByExchangeToken(exchange);
	}

	@GetMapping("/instrument/v1.0/{exchange}/{segment}/{tradingSymbol}")
	public List<InstrumentModel> findByOptionsDB(@PathVariable("exchange") String exchange,
			@PathVariable("segment") String segment, @PathVariable("tradingSymbol") String tradingSymbol) {
		return instrumentRepository.findByOptions(exchange, segment, tradingSymbol);
	}

	@GetMapping("/instrument/v1.0/{exchange}/{instrumentType}/{name}/{segment}/{tradingSymbol}")
	public List<InstrumentModel> findByEquityDB(@PathVariable("exchange") String exchange,
			@PathVariable("instrumentType") String instrumentType, @PathVariable("name") String name,
			@PathVariable("segment") String segment, @PathVariable("tradingSymbol") String tradingSymbol) {
		return instrumentRepository.findByEquity(exchange, instrumentType, name, segment, tradingSymbol);
	}

	@PostMapping({ "/instrument/v1.0" })
	public InstrumentModel add(@RequestBody InstrumentModel instrument) {
		return instrumentRepository.save(instrument);
	}


	@DeleteMapping({ "/instrument/v1.0/{instrumentToken}" })
	public DeleteResult delete(@PathVariable("instrumentToken") String instrumentToken) {
		return instrumentRepository.deleteInstrument(instrumentToken);
	}

	@GetMapping("/instrument/v1.0")
	public List<InstrumentModel> findAll() {
		return instrumentRepository.findAll();
	}

	@GetMapping("/instrument/v1.0/kite/v3.0/{userId}/{requestToken}/{instrumentToken}")
	public List<InstrumentModel> findByInstrumentTokenFromKite(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken, @PathVariable("exchange") String exchange) {
		List<InstrumentModel> list = null;
		try {
			list = DateFormatUtil.convertInstrumentModel(
					tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken).getInstruments(exchange));
		} catch (JSONException | IOException | KiteException e) {
			e.printStackTrace();
		}
		return list;
	}

	@GetMapping("/instrument/v1.0/kite/v3.0/{userId}/{requestToken}")
	public List<InstrumentModel> findAllInstrumentFromKite(@PathVariable("userId") String userId,@PathVariable("requestToken") String requestToken) throws JSONException, IOException, KiteException {
		List<InstrumentModel> list = null;
		instrumentRepository.deleteAll();
		list= DateFormatUtil.convertInstrumentModel(
				tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken).getInstruments());
		for (InstrumentModel instrumentModel : list) {
			instrumentRepository.save(instrumentModel);
		}
		//instrumentRepository.addAllInstruments(list);
		return list;
	}

}
