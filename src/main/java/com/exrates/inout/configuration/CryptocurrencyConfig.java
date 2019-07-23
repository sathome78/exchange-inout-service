package com.exrates.inout.configuration;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.neo.AssetMerchantCurrencyDto;
import com.exrates.inout.domain.neo.NeoAsset;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.*;
import com.exrates.inout.service.BitcoinService;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.achain.AchainContract;
import com.exrates.inout.service.bitshares.BitsharesService;
import com.exrates.inout.service.bitshares.crea.CreaServiceImpl;
import com.exrates.inout.service.btc.BitcoinServiceImpl;
import com.exrates.inout.service.ethereum.EthTokenService;
import com.exrates.inout.service.ethereum.EthTokenServiceImpl;
import com.exrates.inout.service.ethereum.EthereumCommonService;
import com.exrates.inout.service.ethereum.EthereumCommonServiceImpl;
import com.exrates.inout.service.impl.CurrencyServiceImpl;
import com.exrates.inout.service.impl.MerchantServiceImpl;
import com.exrates.inout.service.lisk.ArkRpcClient;
import com.exrates.inout.service.lisk.ArkSpecialMethodServiceImpl;
import com.exrates.inout.service.lisk.LiskRestClient;
import com.exrates.inout.service.lisk.LiskRestClientImpl;
import com.exrates.inout.service.lisk.LiskService;
import com.exrates.inout.service.lisk.LiskServiceImpl;
import com.exrates.inout.service.lisk.LiskSpecialMethodService;
import com.exrates.inout.service.lisk.LiskSpecialMethodServiceImpl;
import com.exrates.inout.service.monero.MoneroService;
import com.exrates.inout.service.monero.MoneroServiceImpl;
import com.exrates.inout.service.monero.hcxp.HCXPServiceImpl;
import com.exrates.inout.service.nem.XemMosaicService;
import com.exrates.inout.service.nem.XemMosaicServiceImpl;
import com.exrates.inout.service.neo.NeoService;
import com.exrates.inout.service.neo.NeoServiceImpl;
import com.exrates.inout.service.qtum.QtumTokenServiceImpl;
import com.exrates.inout.service.stellar.StellarAsset;
import com.exrates.inout.service.tron.TronTrc10Token;
import com.exrates.inout.service.waves.WavesService;
import com.exrates.inout.service.waves.WavesServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class CryptocurrencyConfig {

   private static final Logger log = LogManager.getLogger("config");


    @Autowired
    private CryptoCurrencyProperties ccp;

    @Bean
    public MerchantService merchantService() {
        return new MerchantServiceImpl();
    }

    @Bean
    public CurrencyService currencyService() {
        return new CurrencyServiceImpl();
    }

    @Bean(name = "bitcoinServiceImpl")
    public BitcoinService bitcoinService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBtc());
    }

    @Bean(name = "litecoinServiceImpl")
    public BitcoinService litecoinService() {
        return createBitcoinService(ccp.getBitcoinCoins().getLtc());
    }

    @Bean(name = "dashServiceImpl")
    public BitcoinService dashService() {
        return createBitcoinService(ccp.getBitcoinCoins().getDash());
    }

    @Bean(name = "atbServiceImpl")
    public BitcoinService atbService() {
        return createBitcoinService(ccp.getBitcoinCoins().getAtb());
    }

    @Bean(name = "dogecoinServiceImpl")
    public BitcoinService dogeService() {
        return createBitcoinService(ccp.getBitcoinCoins().getDoge());
    }

    @Bean(name = "btgServiceImpl")
    public BitcoinService btgService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBtg());
    }

    @Bean(name = "zcashServiceImpl")
    public BitcoinService zecService() {
        return createBitcoinService(ccp.getBitcoinCoins().getZec());
    }

    @Bean(name = "b2xServiceImpl")
    public BitcoinService b2xService() {
        return createBitcoinService(ccp.getBitcoinCoins().getB2x());
    }

    @Bean(name = "bcdServiceImpl")
    public BitcoinService bcdService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBcd());
    }

    @Bean(name = "plcServiceImpl")
    public BitcoinService pbtcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getPlc());
    }

    @Bean(name = "bcxServiceImpl")
    public BitcoinService bcxService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBcx());
    }

    @Bean(name = "bciServiceImpl")
    public BitcoinService bciService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBci());
    }

    @Bean(name = "occServiceImpl")
    public BitcoinService occService() {
        return createBitcoinService(ccp.getBitcoinCoins().getOcc());
    }

    @Bean(name = "btczServiceImpl")
    public BitcoinService btczService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBtcz());
    }

    @Bean(name = "lccServiceImpl")
    public BitcoinService lccService() {
        return createBitcoinService(ccp.getBitcoinCoins().getLcc());
    }

    @Bean(name = "bitcoinAtomServiceImpl")
    public BitcoinService bitcoinAtomService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBca());
    }

    @Bean(name = "btcpServiceImpl")
    public BitcoinService btcpService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBtcp());
    }

    @Bean(name = "szcServiceImpl")
    public BitcoinService szcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getSzc());
    }

    @Bean(name = "btxServiceImpl")
    public BitcoinService btxService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBtx());
    }

    @Bean(name = "bitdollarServiceImpl")
    public BitcoinService bitdollarService() {
        return createBitcoinService(ccp.getBitcoinCoins().getXbd());
    }

    @Bean(name = "beetServiceImpl")
    public BitcoinService beetService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBeet());
    }

    @Bean(name = "nycoinServiceImpl")
    public BitcoinService nycoinService() {
        return createBitcoinService(ccp.getBitcoinCoins().getNyc());
    }

    @Bean(name = "ptcServiceImpl")
    public BitcoinService ptcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getPtc());
    }

    @Bean(name = "fgcServiceImpl")
    public BitcoinService fgcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getFgc());
    }

    @Bean(name = "bclServiceImpl")
    public BitcoinService bitcoinCleanService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBcl());
    }

    @Bean(name = "brecoServiceImpl")
    public BitcoinService brecoService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBreco());
    }

    @Bean(name = "ftoServiceImpl")
    public BitcoinService ftoService() {
        return createBitcoinService(ccp.getBitcoinCoins().getFto());
    }

    @Bean(name = "sabrServiceImpl")
    public BitcoinService sabrService() {
        return createBitcoinService(ccp.getBitcoinCoins().getSabr());
    }

    @Bean(name = "eqlServiceImpl")
    public BitcoinService eqlService() {
        return createBitcoinService(ccp.getBitcoinCoins().getEql());
    }

    @Bean(name = "lbtcServiceImpl")
    public BitcoinService lbtcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getLbtc());
    }

    @Bean(name = "brbServiceImpl")
    public BitcoinService brbService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBrb());
    }

    @Bean(name = "rizServiceImpl")
    public BitcoinService rizService() {
        return createBitcoinService(ccp.getBitcoinCoins().getRiz());
    }

    @Bean(name = "sicServiceImpl")
    public BitcoinService sicService() {
        return createBitcoinService(ccp.getBitcoinCoins().getSic());
    }

    @Bean(name = "clxServiceImpl")
    public BitcoinService clxService() {
        return createBitcoinService(ccp.getBitcoinCoins().getClx());
    }

    @Bean(name = "qrkServiceImpl")
    public BitcoinService qrkService() {
        return createBitcoinService(ccp.getBitcoinCoins().getQrk());
    }

    @Bean(name = "cmkServiceImpl")
    public BitcoinService cmkService() {
        return createBitcoinService(ccp.getBitcoinCoins().getCmk());
    }

    @Bean(name = "mbcServiceImpl")
    public BitcoinService mbcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getMbc());
    }

    @Bean(name = "ddxServiceImpl")
    public BitcoinService ddxService() {
        return createBitcoinService(ccp.getBitcoinCoins().getDdx());
    }

    @Bean(name = "lpcServiceImpl")
    public BitcoinService lpcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getLpc());
    }

    @Bean(name = "xfcServiceImpl")
    public BitcoinService xfcServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getXfc());
    }

    @Bean(name = "TOAServiceImpl")
    public BitcoinService taoServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getToa());
    }

    @Bean(name = "crypServiceImpl")
    public BitcoinService crypService() {
        return createBitcoinService(ccp.getBitcoinCoins().getCryp());
    }

    @Bean(name = "cbcServiceImpl")
    public BitcoinService cbcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getCbc());
    }

    @Bean(name = "abbcServiceImpl")
    public BitcoinService abbcService() {
        return createBitcoinService(ccp.getBitcoinCoins().getAbbc());
    }

    @Bean(name = "qServiceImpl")
    public BitcoinService qServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getQ());
    }

    @Bean(name = "dimeServiceImpl")
    public BitcoinService dimeServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getDime());
    }

    @Bean(name = "bsvServiceImpl")
    public BitcoinService bsvServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getBsv());
    }

    @Bean(name = "bchServiceImpl")
    public BitcoinService bchServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getBch());
    }

    @Bean(name = "ctxServiceImpl")
    public BitcoinService ctxServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getCtx());
    }

    @Bean(name = "rimeServiceImpl")
    public BitcoinService rimeServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getRime());
    }

    @Bean(name = "exoServiceImpl")
    public BitcoinService exoServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getExo());
    }

    @Bean(name = "grsServiceImpl")
    public BitcoinService grsServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getGrs());
    }

    @Bean(name = "kodServiceImpl")
    public BitcoinService kodServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getKod());
    }

    @Bean(name = "diviServiceImpl")
    public BitcoinService diviServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getDivi());
    }

    @Bean(name = "owcServiceImpl")
    public BitcoinService owcServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getOwc());
    }

    @Bean(name = "wolfServiceImpl")
    public BitcoinService wolfServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getWolf());
    }


    @Bean(name = "tslServiceImpl")
    public BitcoinService tslServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getTsl());
    }

    @Bean(name = "vollarServiceImpl")
    public BitcoinService vollarServiceImpl() {
        return createBitcoinService(ccp.getBitcoinCoins().getVollar());
    }

    private BitcoinService createBitcoinService(BitcoinProperty property) {
        return new BitcoinServiceImpl(property);
    }

    //ETH Services
    @Bean(name = "ethereumServiceImpl")
    public EthereumCommonService ethereumService() {
        return createEthereumService(ccp.getEthereumCoins().getEth());
    }

    @Bean(name = "ethereumClassicServiceImpl")
    public EthereumCommonService ethereumClassicService() {
        return createEthereumService(ccp.getEthereumCoins().getEtc());
    }

    @Bean(name = "etzServiceImpl")
    public EthereumCommonService etzService() {
        return createEthereumService(ccp.getEthereumCoins().getEtz());
    }

    @Bean(name = "cloServiceImpl")
    public EthereumCommonService cloService() {
        return createEthereumService(ccp.getEthereumCoins().getClo());
    }

    @Bean(name = "b2gServiceImpl")
    public EthereumCommonService b2gService() {
        return createEthereumService(ccp.getEthereumCoins().getB2g());
    }

    @Bean(name = "golServiceImpl")
    public EthereumCommonService golService() {
        return createEthereumService(ccp.getEthereumCoins().getGol());
    }

    @Bean(name = "cnetServiceImpl")
    public EthereumCommonService cnetService() {
        return createEthereumService(ccp.getEthereumCoins().getCnet());
    }

    @Bean(name = "ntyServiceImpl")
    public EthereumCommonService ntyService() {
        return createEthereumService(ccp.getEthereumCoins().getNty());
    }

    @Bean(name = "etherincServiceImpl")
    public EthereumCommonService etherincService() {
        return createEthereumService(ccp.getEthereumCoins().getEti());
    }

    private EthereumCommonServiceImpl createEthereumService(EthereumProperty property) {
        return new EthereumCommonServiceImpl(property);
    }

        //Eth tokens
    @Bean(name = "repServiceImpl")
    public EthTokenService RepService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getREP());
    }

    @Bean(name = "golemServiceImpl")
    public EthTokenService GolemService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGOLEM());
    }

    @Bean(name = "omgServiceImpl")
    public EthTokenService OmgService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getOMG());
    }

    @Bean(name = "bnbServiceImpl")
    public EthTokenService BnbService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBINANCECOIN());
    }

    @Bean(name = "atlServiceImpl")
    public EthTokenService ATLANTService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getATLANT());
    }

    @Bean(name = "bitRentServiceImpl")
    public EthTokenService BitRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBITRENT());
    }

    @Bean(name = "nioServiceImpl")
    public EthTokenService NioService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNIO());
    }

    @Bean(name = "gosServiceImpl")
    public EthTokenService GosService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGOS());
    }

    @Bean(name = "bptnServiceImpl")
    public EthTokenService BptnRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBPTN());
    }

    @Bean(name = "nbcServiceImpl")
    public EthTokenService NbcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNBC());
    }

    @Bean(name = "taxiServiceImpl")
    public EthTokenService taxiRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTAXI());
    }

    @Bean(name = "nbtkServiceImpl")
    public EthTokenService nbtkRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNBTK());
    }

    @Bean(name = "ucashServiceImpl")
    public EthTokenService ucashService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUCASH());
    }

    @Bean(name = "nacServiceImpl")
    public EthTokenService nacService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNAC());
    }

    @Bean(name = "echtServiceImpl")
    public EthTokenService echtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getECHT());
    }

    @Bean(name = "idhServiceImpl")
    public EthTokenService idhService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIDH());
    }

    @Bean(name = "cobcServiceImpl")
    public EthTokenService cobcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCOBC());
    }

    @Bean(name = "bcsServiceImpl")
    public EthTokenService bcsService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBCS());
    }

    @Bean(name = "uqcServiceImpl")
    public EthTokenService uqcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUQC());
    }

    @Bean(name = "inoServiceImpl")
    public EthTokenService inoService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getINO());
    }

    @Bean(name = "profitServiceImpl")
    public EthTokenService profitService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getPROFIT());
    }

    @Bean(name = "ormeServiceImpl")
    public EthTokenService ormeService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getORME());
    }

    @Bean(name = "bezServiceImpl")
    public EthTokenService bezService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBEZ());
    }

    @Bean(name = "simServiceImpl")
    public EthTokenService simService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSIM());
    }

    @Bean(name = "amnServiceImpl")
    public EthTokenService amnService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getAMN());
    }

    @Bean(name = "getServiceImpl")
    public EthTokenService getService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGET());
    }

    @Bean(name = "flotServiceImpl")
    public EthTokenService flotService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getFLOT());
    }

    @Bean(name = "vdgServiceImpl")
    public EthTokenService vdgService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getVDG());
    }

    @Bean(name = "dgtxServiceImpl")
    public EthTokenService dgtxService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getDGTX());
    }

    @Bean(name = "droneServiceImpl")
    public EthTokenService droneService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getDRONE());
    }

    @Bean(name = "wdscServiceImpl")
    public EthTokenService wdscService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getWDSC());
    }

    @Bean(name = "fsbtServiceImpl")
    public EthTokenService fsbtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getFSBT());
    }

    @Bean(name = "iprServiceImpl")
    public EthTokenService iprService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIPR());
    }

    @Bean(name = "casServiceImpl")
    public EthTokenService casService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCAS());
    }

    @Bean(name = "tnrServiceImpl")
    public EthTokenService tnrService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTNR());
    }

    @Bean(name = "inkServiceImpl")
    public EthTokenService inkService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getINK());
    }

    @Bean(name = "rthServiceImpl")
    public EthTokenService rthService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRTH());
    }

    @Bean(name = "spdServiceImpl")
    public EthTokenService spdService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSPD());
    }

    @Bean(name = "mtcServiceImpl")
    public EthTokenService mtcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMTC());
    }

    @Bean(name = "arnServiceImpl")
    public EthTokenService arnService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getARN());
    }

    @Bean(name = "hstServiceImpl")
    public EthTokenService hstService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getHST());
    }

    @Bean(name = "dtrcServiceImpl")
    public EthTokenService dtrcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getDTRC());
    }

    @Bean(name = "ceekServiceImpl")
    public EthTokenService ceekService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCEEK());
    }

    @Bean(name = "anyServiceImpl")
    public EthTokenService anyService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getANY());
    }

    @Bean(name = "tgameServiceImpl")
    public EthTokenService tgameService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTGAME());
    }

    @Bean(name = "mtlServiceImpl")
    public EthTokenService mtlService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMTL());
    }

    @Bean(name = "leduServiceImpl")
    public EthTokenService leduService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getLEDU());
    }

    @Bean(name = "adbServiceImpl")
    public EthTokenService adbService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getADB());
    }

    @Bean(name = "cedexServiceImpl")
    public EthTokenService cedexService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCEDEX());
    }

    @Bean(name = "gstServiceImpl")
    public EthTokenService gstService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGST());
    }

    @Bean(name = "satServiceImpl")
    public EthTokenService satService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSAT());
    }

    @Bean(name = "cheServiceImpl")
    public EthTokenService cheService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCHE());
    }

    @Bean(name = "daccServiceImpl")
    public EthTokenService daccService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getDACC());
    }

    @Bean(name = "engtServiceImpl")
    public EthTokenService engtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getENGT());
    }

    @Bean(name = "tavittServiceImpl")
    public EthTokenService tavittService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTAVITT());
    }

    @Bean(name = "umtServiceImpl")
    public EthTokenService umtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUMT());
    }

    @Bean(name = "maspServiceImpl")
    public EthTokenService maspService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMASP());
    }

    @Bean(name = "skillServiceImpl")
    public EthTokenService skillService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSKILL());
    }

    @Bean(name = "storServiceImpl")
    public EthTokenService storService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSTOR());
    }

    @Bean(name = "quintServiceImpl")
    public EthTokenService quintService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getQUINT());
    }

    @Bean(name = "ttcServiceImpl")
    public EthTokenService ttcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTTC());
    }

    @Bean(name = "bfgServiceImpl")
    public EthTokenService bfgService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBFG());
    }

    @Bean(name = "jetServiceImpl")
    public EthTokenService jetService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getJET());
    }

    @Bean(name = "patServiceImpl")
    public EthTokenService patService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getPAT());
    }

    @Bean(name = "emtvServiceImpl")
    public EthTokenService emtvService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getEMTV());
    }

    @Bean(name = "kwattServiceImpl")
    public EthTokenService kwattService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getKWATT());
    }

    @Bean(name = "tusdServiceImpl")
    public EthTokenService tusdService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTUSD());
    }

    @Bean(name = "fpwrServiceImpl")
    public EthTokenService fpwrService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getFPWR());
    }

    @Bean(name = "crbtServiceImpl")
    public EthTokenService crbtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCRBT());
    }

    @Bean(name = "hiveServiceImpl")
    public EthTokenService hiveService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getHIVE());
    }

    @Bean(name = "cmitServiceImpl")
    public EthTokenService cmitService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCMIT());
    }

    @Bean(name = "hdrServiceImpl")
    public EthTokenService hdrService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getHDR());
    }

    @Bean(name = "racServiceImpl")
    public EthTokenService racService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRAC());
    }

    @Bean(name = "iqnServiceImpl")
    public EthTokenService iqnService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIQN());
    }

    @Bean(name = "gexServiceImpl")
    public EthTokenService gexService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGEX());
    }

    @Bean(name = "ixeServiceImpl")
    public EthTokenService ixeService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIXE());
    }

    @Bean(name = "nerServiceImpl")
    public EthTokenService nerService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNER());
    }

    @Bean(name = "phiServiceImpl")
    public EthTokenService phiService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getPHI());
    }

    @Bean(name = "retServiceImpl")
    public EthTokenService retService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRET());
    }

    @Bean(name = "mftuServiceImpl")
    public EthTokenService mftuService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMFTU());
    }

    @Bean(name = "gigcServiceImpl")
    public EthTokenService gigcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGIGC());
    }

    @Bean(name = "swmServiceImpl")
    public EthTokenService swmService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSWM());
    }

    @Bean(name = "ticServiceImpl")
    public EthTokenService ticService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTIC());
    }

    @Bean(name = "bncServiceImpl")
    public EthTokenService bncService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBNC());
    }

    @Bean(name = "wtlServiceImpl")
    public EthTokenService wtlService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getWTL());
    }

    @Bean(name = "udooServiceImpl")
    public EthTokenService udooService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUDOO());
    }

    @Bean(name = "xauServiceImpl")
    public EthTokenService xauService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getXAU());
    }

    @Bean(name = "usdcServiceImpl")
    public EthTokenService usdcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUSDC());
    }

    @Bean(name = "ttpServiceImpl")
    public EthTokenService ttpService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTTP());
    }

    @Bean(name = "mgxServiceImpl")
    public EthTokenService mgxService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMGX());
    }

    @Bean(name = "vaiServiceImpl")
    public EthTokenService vaiService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getVAI());
    }

    @Bean(name = "uncServiceImpl")
    public EthTokenService uncService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUNC());
    }

    @Bean(name = "modlServiceImpl")
    public EthTokenService modlService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMODL());
    }

    @Bean(name = "ecteServiceImpl")
    public EthTokenService ecteService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getECTE());
    }

    @Bean(name = "s4fServiceImpl")
    public EthTokenService s4fService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getS4F());
    }

    @Bean(name = "mncServiceImpl")
    public EthTokenService mncService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMNC());
    }

    @Bean(name = "tcatServiceImpl")
    public EthTokenService tcatService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTCAT());
    }

    @Bean(name = "htServiceImpl")
    public EthTokenService htService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getHT());
    }

    @Bean(name = "edtServiceImpl")
    public EthTokenService edtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getEDT());
    }

    @Bean(name = "poaServiceImpl")
    public EthTokenService poaService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getPOA());
    }

    @Bean(name = "mcoServiceImpl")
    public EthTokenService mcoService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMCO());
    }

    @Bean(name = "zilServiceImpl")
    public EthTokenService zilService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getZIL());
    }

    @Bean(name = "manaServiceImpl")
    public EthTokenService manaService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMANA());
    }

    @Bean(name = "wabiServiceImpl")
    public EthTokenService wabiService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getWABI());
    }

    @Bean(name = "npxsServiceImpl")
    public EthTokenService npxsService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNPXS());
    }

    @Bean(name = "qkcServiceImpl")
    public EthTokenService qkcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getQKC());
    }

    @Bean(name = "hotServiceImpl")
    public EthTokenService hotService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getHOT());
    }

    @Bean(name = "zrxServiceImpl")
    public EthTokenService zrxService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getZRX());
    }

    @Bean(name = "batServiceImpl")
    public EthTokenService batService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBAT());
    }

    @Bean(name = "rdnServiceImpl")
    public EthTokenService rdnService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRDN());
    }

    @Bean(name = "hniServiceImpl")
    public EthTokenService hniService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getHNI());
    }

    @Bean(name = "eltServiceImpl")
    public EthTokenService eltService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getELT());
    }

    @Bean(name = "renServiceImpl")
    public EthTokenService renService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getREN());
    }

    @Bean(name = "metServiceImpl")
    public EthTokenService metService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getMET());
    }

    @Bean(name = "pltcServiceImpl")
    public EthTokenService pltcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getPLTC());
    }

    @Bean(name = "vrbsServiceImpl")
    public EthTokenService vrbsService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getVRBS());
    }

    @Bean(name = "zubeServiceImpl")
    public EthTokenService zubeService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getZUBE());
    }

    @Bean(name = "elcServiceImpl")
    public EthTokenService elcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getELC());
    }

    @Bean(name = "tttServiceImpl")
    public EthTokenService tttService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTTT());
    }

    @Bean(name = "rebServiceImpl")
    public EthTokenService rebService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getREB());
    }

    @Bean(name = "rvcServiceImpl")
    public EthTokenService rvcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRVC());
    }

    @Bean(name = "bioServiceImpl")
    public EthTokenService bioService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBIO());
    }

    @Bean(name = "vraServiceImpl")
    public EthTokenService vraService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getVRA());
    }

    @Bean(name = "katServiceImpl")
    public EthTokenService katService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getKAT());
    }

    @Bean(name = "etaServiceImpl")
    public EthTokenService etaService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getETA());
    }

    @Bean(name = "brcServiceImpl")
    public EthTokenService brcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBRC());
    }

    @Bean(name = "gnyServiceImpl")
    public EthTokenService gnyService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGNY());
    }

    @Bean(name = "novaServiceImpl")
    public EthTokenService novaService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNOVA());
    }

    @Bean(name = "rvtServiceImpl")
    public EthTokenService rvtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRVT());
    }

    @Bean(name = "linaServiceImpl")
    public EthTokenService linaService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getLINA());
    }

    @Bean(name = "gapiServiceImpl")
    public EthTokenService gapiService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGAPI());
    }

    @Bean(name = "embrServiceImpl")
    public EthTokenService embrService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getEMBR());
    }

    @Bean(name = "usdtServiceImpl")
    public EthTokenService usdtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUSDT());
    }

    @Bean(name = "asgServiceImpl")
    public EthTokenService asgService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getASG());
    }

    private EthTokenService createEthereumTokenService(EthereumTokenProperty property) {
        String[] s = property.getContract().replaceAll(" ", "").split(",");
        List<String> contractAddress = Arrays.asList(s);
        return new EthTokenServiceImpl(contractAddress,
                property.getMerchantName(),
                property.getCurrencyName(),
                property.isERC20(),
                property.getUnit(),
                property.getMinWalletBalance());
    }

//     LISK-like cryptos
    @Bean(name = "liskServiceImpl")
    public LiskService liskService() {
        return createLiskService(ccp.getLiskCoins().getLisk(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "btwServiceImpl")
    public LiskService btwService() {
        return createLiskService(ccp.getLiskCoins().getBtw(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "riseServiceImpl")
    public LiskService riseService() {
        return createLiskService(ccp.getLiskCoins().getRise(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "arkServiceImpl")
    public LiskService arkService(ArkRpcClient arkRpcClient) {
        return createLiskService(ccp.getLiskCoins().getArk(), new ArkSpecialMethodServiceImpl(ccp.getLiskCoins().getArk().getArcNode(), arkRpcClient));
    }

    private LiskService createLiskService(LiskProperty property, LiskSpecialMethodService liskSpecialMethodService) {
        return new LiskServiceImpl(liskRestClient(), liskSpecialMethodService, property.getMerchantName(), property.getCurrencyName(), property);
    }

    @Bean
    @Scope("prototype")
    public LiskRestClient liskRestClient() {
        return new LiskRestClientImpl();
    }

    // WAVES-like
    @Bean(name = "wavesServiceImpl")
    public WavesService wavesService() {
        return createWavesService(ccp.getWavesCoins().getWaves());
    }

    @Bean(name = "lunesServiceImpl")
    public WavesService lunesService() {
        return createWavesService(ccp.getWavesCoins().getLunes());
    }

    private WavesService createWavesService(WavesProperty property) {
        return new WavesServiceImpl(property);
    }

    //NEO and Forks
    @Bean(name = "neoServiceImpl")
    public NeoService neoService() {
        Merchant mainMerchant = merchantService().findByName(NeoAsset.NEO.name());
        Currency mainCurrency = currencyService().findByName(NeoAsset.NEO.name());

        Map<String, AssetMerchantCurrencyDto> neoAssetMap = new HashMap<String, AssetMerchantCurrencyDto>() {{
            put(NeoAsset.NEO.getId(), new AssetMerchantCurrencyDto(NeoAsset.NEO, mainMerchant, mainCurrency));
            put(NeoAsset.GAS.getId(), new AssetMerchantCurrencyDto(NeoAsset.GAS, merchantService().findByName(NeoAsset.GAS.name()), currencyService().findByName(NeoAsset.GAS.name())));
        }};
        NeoProperty property = ccp.getNeoCoins().getNeo().toBuilder()
                .merchant(mainMerchant)
                .currency(mainCurrency)
                .neoAssetMap(neoAssetMap)
                .build();
        return new NeoServiceImpl(property);
    }

    @Bean(name = "kazeServiceImpl")
    public NeoService kazeService() {
        Merchant mainMerchant = merchantService().findByName(NeoAsset.KAZE.name());
        Currency mainCurrency = currencyService().findByName(NeoAsset.KAZE.name());

        Map<String, AssetMerchantCurrencyDto> neoAssetMap = new HashMap<String, AssetMerchantCurrencyDto>() {{
            put(NeoAsset.KAZE.getId(), new AssetMerchantCurrencyDto(NeoAsset.KAZE, mainMerchant, mainCurrency));
            put(NeoAsset.STREAM.getId(), new AssetMerchantCurrencyDto(NeoAsset.STREAM, merchantService().findByName(NeoAsset.STREAM.name()), currencyService().findByName(NeoAsset.STREAM.name())));
        }};
        NeoProperty property = ccp.getNeoCoins().getNeo().toBuilder()
                .merchant(mainMerchant)
                .currency(mainCurrency)
                .neoAssetMap(neoAssetMap)
                .build();
        return new NeoServiceImpl(property);
    }

    @Bean(name = "cronServiceImpl")
    public NeoService cronService() {
        Merchant mainMerchant = merchantService().findByName(NeoAsset.CRON.name());
        Currency mainCurrency = currencyService().findByName(NeoAsset.CRON.name());
        Map<String, AssetMerchantCurrencyDto> neoAssetMap = new HashMap<String, AssetMerchantCurrencyDto>() {{
            put(NeoAsset.CRON.getId(), new AssetMerchantCurrencyDto(NeoAsset.CRON,
                    merchantService().findByName(NeoAsset.CRON.name()), currencyService().findByName(NeoAsset.CRON.name())));
        }};
        NeoProperty property = ccp.getNeoCoins().getNeo().toBuilder()
                .merchant(mainMerchant)
                .currency(mainCurrency)
                .neoAssetMap(neoAssetMap)
                .build();
        return new NeoServiceImpl(property);
    }


    @Bean(name = "bitTorrentServiceImpl")
    public TronTrc10Token bitTorrentService() {
        return new TronTrc10Token("BTT", "BTT", 6, "1002000", "31303032303030", "1002000");
    }

    //Qtum tokens:
    @Bean(name = "spcServiceImpl")
    public QtumTokenServiceImpl spcService() {
        return createQtumService(ccp.getQtumCoins().getSpc());
    }

    @Bean(name = "hlcServiceImpl")
    public QtumTokenServiceImpl hlcService() {
        return createQtumService(ccp.getQtumCoins().getHlc());
    }

    private QtumTokenServiceImpl createQtumService(QtumTokenProperty property) {
        return new QtumTokenServiceImpl(property, ccp.getOtherCoins().getQtum());
    }

    //**** Monero ****/
    @Bean(name = "moneroServiceImpl")
    public MoneroService moneroService() {
        return createMoneroService(ccp.getMoneroCoins().getXmr());
    }

    @Bean(name = "ditcoinServiceImpl")
    public MoneroService ditcoinService() {
        return createMoneroService(ccp.getMoneroCoins().getDit());
    }

    @Bean(name = "sumoServiceImpl")
    public MoneroService sumoService() {
        return createMoneroService(ccp.getMoneroCoins().getSumo());
    }

    @Bean(name = "hcxpServiceImpl")
    public MoneroService hcxpService() {
        return new HCXPServiceImpl(ccp.getMoneroCoins().getHcxp());
    }

    private MoneroService createMoneroService(MoneroProperty property) {
        return new MoneroServiceImpl(property);
    }

    /***tokens based on xem mosaic)****/
    @Bean(name = "dimCoinServiceImpl")
    public XemMosaicService dimCoinService() {
        return createXemMosaicService(ccp.getXemCoins().getDim());
    }

    @Bean(name = "npxsServiceImpl")
    public XemMosaicService npxsServiceImpl() {
        return createXemMosaicService(ccp.getXemCoins().getNpxs());
    }

    @Bean(name = "dimEurServiceImpl")
    public XemMosaicService dimEurService() {
        return createXemMosaicService(ccp.getXemCoins().getDIM_EUR());
    }

    @Bean(name = "dimUsdServiceImpl")
    public XemMosaicService dimUsdService() {
        return createXemMosaicService(ccp.getXemCoins().getDIM_USD());
    }

    @Bean(name = "digicServiceImpl")
    public XemMosaicService digicService() { return createXemMosaicService(ccp.getXemCoins().getDIGIC()); }

    @Bean(name = "darcServiceImpl")
    public XemMosaicService darcService() { return createXemMosaicService(ccp.getXemCoins().getDARC()); }

    @Bean(name = "rwdsServiceImpl")
    public XemMosaicService rwdsService() { return createXemMosaicService(ccp.getXemCoins().getRWDS()); }

    private XemMosaicService createXemMosaicService(XemProperty property) {
        return new XemMosaicServiceImpl(property);
    }

    @Bean(name = "sltStellarService")
    public StellarAsset sltStellarService() {
        return createStellarService(ccp.getStellarCoins().getSlt());
    }

    @Bean(name = "ternStellarService")
    public StellarAsset ternStellarService() {
        return createStellarService(ccp.getStellarCoins().getTern());
    }

    @Bean(name = "vntStellarService")
    public StellarAsset vntStellarService() {
        return createStellarService(ccp.getStellarCoins().getVnt());
    }

    private StellarAsset createStellarService(StellarAssetProperty property) {
        return new StellarAsset(property);
    }

    @Bean("vexaniumContract")
    public AchainContract achainContractService() {
        return new AchainContract("ACT9XnhX5FtQqGFAa3KgrgkPCCEDPmuzgtSx", "VEX", "VEX", "Vexanium_Token");
    }

    //Bithsares
    @Bean(name = "creaServiceImpl")
    public BitsharesService bitsharesService(){
        return new CreaServiceImpl("CREA", "CREA", ccp.getBitsharesCoins().getCrea(), 6, 3);
    }
}
