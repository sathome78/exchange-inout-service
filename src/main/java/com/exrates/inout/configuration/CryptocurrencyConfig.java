package com.exrates.inout.configuration;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.neo.AssetMerchantCurrencyDto;
import com.exrates.inout.domain.neo.NeoAsset;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.BitcoinProperty;
import com.exrates.inout.properties.models.EthereumProperty;
import com.exrates.inout.properties.models.EthereumTokenProperty;
import com.exrates.inout.properties.models.LiskProperty;
import com.exrates.inout.properties.models.MoneroProperty;
import com.exrates.inout.properties.models.NeoProperty;
import com.exrates.inout.properties.models.QtumProperty;
import com.exrates.inout.properties.models.WavesProperty;
import com.exrates.inout.properties.models.XemProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.achain.AchainContract;
import com.exrates.inout.service.btc.BitcoinService;
import com.exrates.inout.service.btc.BitcoinServiceImpl;
import com.exrates.inout.service.crypto_currencies.MoneroService;
import com.exrates.inout.service.crypto_currencies.MoneroServiceImpl;
import com.exrates.inout.service.ethereum.EthTokenService;
import com.exrates.inout.service.ethereum.EthTokenServiceImpl;
import com.exrates.inout.service.ethereum.EthereumCommonService;
import com.exrates.inout.service.ethereum.EthereumCommonServiceImpl;
import com.exrates.inout.service.lisk.ArkSpecialMethodServiceImpl;
import com.exrates.inout.service.lisk.LiskRestClient;
import com.exrates.inout.service.lisk.LiskRestClientImpl;
import com.exrates.inout.service.lisk.LiskService;
import com.exrates.inout.service.lisk.LiskServiceImpl;
import com.exrates.inout.service.lisk.LiskSpecialMethodService;
import com.exrates.inout.service.lisk.LiskSpecialMethodServiceImpl;
import com.exrates.inout.service.nem.XemMosaicService;
import com.exrates.inout.service.nem.XemMosaicServiceImpl;
import com.exrates.inout.service.neo.NeoService;
import com.exrates.inout.service.neo.NeoServiceImpl;
import com.exrates.inout.service.qtum.QtumTokenService;
import com.exrates.inout.service.qtum.QtumTokenServiceImpl;
import com.exrates.inout.service.stellar.StellarAsset;
import com.exrates.inout.service.waves.WavesService;
import com.exrates.inout.service.waves.WavesServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;


@Log4j2(topic = "config")
@Configuration
public class CryptocurrencyConfig {

    @Autowired
    private CryptoCurrencyProperties cryptoCurrencyProperties;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;

    @Bean(name = "bitcoinServiceImpl")
    public BitcoinService bitcoinService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBtc());
    }

    @Bean(name = "litecoinServiceImpl")
    public BitcoinService litecoinService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getLtc());
    }

    @Bean(name = "dashServiceImpl")
    public BitcoinService dashService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getDash());
    }

    @Bean(name = "atbServiceImpl")
    public BitcoinService atbService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getAtb());
    }

    @Bean(name = "bitcoinCashServiceImpl")
    public BitcoinService bchService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBch());
    }

    @Bean(name = "dogecoinServiceImpl")
    public BitcoinService dogeService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getDoge());
    }

    @Bean(name = "btgServiceImpl")
    public BitcoinService btgService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBtg());
    }

    @Bean(name = "zcashServiceImpl")
    public BitcoinService zecService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getZec());
    }

    @Bean(name = "b2xServiceImpl")
    public BitcoinService b2xService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getB2x());
    }

    @Bean(name = "bcdServiceImpl")
    public BitcoinService bcdService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBcd());
    }

    @Bean(name = "plcServiceImpl")
    public BitcoinService pbtcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getPlc());
    }

    @Bean(name = "bcxServiceImpl")
    public BitcoinService bcxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBcx());
    }

    @Bean(name = "bciServiceImpl")
    public BitcoinService bciService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBci());
    }

    @Bean(name = "occServiceImpl")
    public BitcoinService occService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getOcc());
    }

    @Bean(name = "btczServiceImpl")
    public BitcoinService btczService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBtcz());
    }

    @Bean(name = "lccServiceImpl")
    public BitcoinService lccService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getLcc());
    }

    @Bean(name = "bitcoinAtomServiceImpl")
    public BitcoinService bitcoinAtomService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBca());
    }

    @Bean(name = "btcpServiceImpl")
    public BitcoinService btcpService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBtcp());
    }

    @Bean(name = "szcServiceImpl")
    public BitcoinService szcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getSzc());
    }

    @Bean(name = "btxServiceImpl")
    public BitcoinService btxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBtx());
    }

    @Bean(name = "bitdollarServiceImpl")
    public BitcoinService bitdollarService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getXbd());
    }

    @Bean(name = "beetServiceImpl")
    public BitcoinService beetService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBeet());
    }

    @Bean(name = "nycoinServiceImpl")
    public BitcoinService nycoinService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getNyc());
    }

    @Bean(name = "ptcServiceImpl")
    public BitcoinService ptcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getPtc());
    }

    @Bean(name = "fgcServiceImpl")
    public BitcoinService fgcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getFgc());
    }

    @Bean(name = "bclServiceImpl")
    public BitcoinService bitcoinCleanService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBcl());
    }

    @Bean(name = "brecoServiceImpl")
    public BitcoinService brecoService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBreco());
    }

    @Bean(name = "ftoServiceImpl")
    public BitcoinService ftoService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getFto());
    }

    @Bean(name = "sabrServiceImpl")
    public BitcoinService sabrService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getSabr());
    }

    @Bean(name = "eqlServiceImpl")
    public BitcoinService eqlService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getEql());
    }

    @Bean(name = "lbtcServiceImpl")
    public BitcoinService lbtcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getLbtc());
    }

    @Bean(name = "brbServiceImpl")
    public BitcoinService brbService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBrb());
    }

    @Bean(name = "rizServiceImpl")
    public BitcoinService rizService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getRiz());
    }

    @Bean(name = "sicServiceImpl")
    public BitcoinService sicService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getSic());
    }

    @Bean(name = "clxServiceImpl")
    public BitcoinService clxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getClx());
    }

    @Bean(name = "qrkServiceImpl")
    public BitcoinService qrkService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getQrk());
    }

    @Bean(name = "cmkServiceImpl")
    public BitcoinService cmkService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getCmk());
    }

    @Bean(name = "mbcServiceImpl")
    public BitcoinService mbcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getMbc());
    }

    @Bean(name = "ddxServiceImpl")
    public BitcoinService ddxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getDdx());
    }

    @Bean(name = "lpcServiceImpl")
    public BitcoinService lpcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getLpc());
    }

    @Bean(name = "xfcServiceImpl")
    public BitcoinService xfcServiceImpl() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getXfc());
    }

    @Bean(name = "TOAServiceImpl")
    public BitcoinService taoServiceImpl() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getToa());
    }

    @Bean(name = "crypServiceImpl")
    public BitcoinService crypService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getCryp());
    }

    @Bean(name = "nsrServiceImpl")
    public BitcoinService nsrService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getNsr());
    }

    @Bean(name = "amlServiceImpl")
    public BitcoinService amlService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getAml());
    }

    @Bean(name = "bbccServiceImpl")
    public BitcoinService bbccService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getBbx());
    }

    @Bean(name = "hsrServiceImpl")
    public BitcoinService hcasheService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinMerchants().getHsr());
    }

    private BitcoinService createBitcoinService(BitcoinProperty property) {
        return new BitcoinServiceImpl(property);
    }

    //ETH Services
    @Bean(name = "ethereumServiceImpl")
    public EthereumCommonService ethereumService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getEth());
    }

    @Bean(name = "ethereumClassicServiceImpl")
    public EthereumCommonService ethereumClassicService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getEtc());
    }

    @Bean(name = "etzServiceImpl")
    public EthereumCommonService etzService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getEtz());
    }

    @Bean(name = "cloServiceImpl")
    public EthereumCommonService cloService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getClo());
    }

    @Bean(name = "b2gServiceImpl")
    public EthereumCommonService b2gService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getB2g());
    }

    @Bean(name = "golServiceImpl")
    public EthereumCommonService golService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getGol());
    }

    @Bean(name = "cnetServiceImpl")
    public EthereumCommonService cnetService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getCnet());
    }

    @Bean(name = "ntyServiceImpl")
    public EthereumCommonService ntyService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getNty());
    }

    @Bean(name = "etherincServiceImpl")
    public EthereumCommonService etherincService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumMerchants().getEti());
    }

    private EthereumCommonService createEthereumCommonService(EthereumProperty property) {
        return new EthereumCommonServiceImpl(property);
    }

    //Eth tokens
    @Bean(name = "repServiceImpl")
    public EthTokenService RepService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getRep());
    }

    @Bean(name = "golemServiceImpl")
    public EthTokenService GolemService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getGolem());
    }

    @Bean(name = "omgServiceImpl")
    public EthTokenService OmgService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getOmg());
    }

    @Bean(name = "bnbServiceImpl")
    public EthTokenService BnbService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getBnb());
    }

    @Bean(name = "atlServiceImpl")
    public EthTokenService ATLANTService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getAtl());
    }

    @Bean(name = "bitRentServiceImpl")
    public EthTokenService BitRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getRntb());
    }

    @Bean(name = "nioServiceImpl")
    public EthTokenService NioService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getNio());
    }

    @Bean(name = "gosServiceImpl")
    public EthTokenService GosService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getGos());
    }

    @Bean(name = "bptnServiceImpl")
    public EthTokenService BptnRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getBptn());
    }

    @Bean(name = "nbcServiceImpl")
    public EthTokenService NbcService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getNbc());
    }

    @Bean(name = "taxiServiceImpl")
    public EthTokenService taxiRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getTaxi());
    }

    @Bean(name = "nbtkServiceImpl")
    public EthTokenService nbtkRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getNbtk());
    }

    @Bean(name = "ucashServiceImpl")
    public EthTokenService ucashService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getUcash());
    }

    @Bean(name = "nacServiceImpl")
    public EthTokenService nacService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getNac());
    }

    @Bean(name = "echtServiceImpl")
    public EthTokenService echtService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getEcht());
    }

    @Bean(name = "idhServiceImpl")
    public EthTokenService idhService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getIdh());
    }

    @Bean(name = "cobcServiceImpl")
    public EthTokenService cobcService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getCobc());
    }

    @Bean(name = "bcsServiceImpl")
    public EthTokenService bcsService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getBcs());
    }

    @Bean(name = "uqcServiceImpl")
    public EthTokenService uqcService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getUqc());
    }

    @Bean(name = "inoServiceImpl")
    public EthTokenService inoService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getIno());
    }

    @Bean(name = "profitServiceImpl")
    public EthTokenService profitService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getProfit());
    }

    @Bean(name = "ormeServiceImpl")
    public EthTokenService ormeService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getOrme());
    }

    @Bean(name = "bezServiceImpl")
    public EthTokenService bezService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getBez());
    }

    @Bean(name = "simServiceImpl")
    public EthTokenService simService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getSim());
    }

    @Bean(name = "amnServiceImpl")
    public EthTokenService amnService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getAmn());
    }

    @Bean(name = "getServiceImpl")
    public EthTokenService getService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getGet());
    }

    @Bean(name = "flotServiceImpl")
    public EthTokenService flotService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getFlot());
    }

    @Bean(name = "vdgServiceImpl")
    public EthTokenService vdgService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getVdg());
    }

    @Bean(name = "dgtxServiceImpl")
    public EthTokenService dgtxService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getDgtx());
    }

    @Bean(name = "droneServiceImpl")
    public EthTokenService droneService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getDrone());
    }

    @Bean(name = "wdscServiceImpl")
    public EthTokenService wdscService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getWdsc());
    }

    @Bean(name = "fsbtServiceImpl")
    public EthTokenService fsbtService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getFsbt());
    }

    @Bean(name = "iprServiceImpl")
    public EthTokenService iprService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getIpr());
    }

    @Bean(name = "casServiceImpl")
    public EthTokenService casService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getCas());
    }

    @Bean(name = "tnrServiceImpl")
    public EthTokenService tnrService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenMerchants().getTnr());
    }

    private EthTokenService createEthereumTokenService(EthereumTokenProperty property) {
        return new EthTokenServiceImpl(property);
    }

    // LISK-like cryptos
    @Bean(name = "liskServiceImpl")
    public LiskService liskService() {
        return createLiskService(cryptoCurrencyProperties.getLiskMerchants().getLisk(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "btwServiceImpl")
    public LiskService btwService() {
        return createLiskService(cryptoCurrencyProperties.getLiskMerchants().getBtw(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "riseServiceImpl")
    public LiskService riseService() {
        return createLiskService(cryptoCurrencyProperties.getLiskMerchants().getRise(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "arkServiceImpl")
    public LiskService arkService() {
        return createLiskService(cryptoCurrencyProperties.getLiskMerchants().getArk(), new ArkSpecialMethodServiceImpl(cryptoCurrencyProperties.getLiskMerchants().getArk()));
    }

    private LiskService createLiskService(LiskProperty property, LiskSpecialMethodService liskSpecialMethodService) {
        return LiskServiceImpl.builder()
                .property(property)
                .liskRestClient(liskRestClient())
                .liskSpecialMethodService(liskSpecialMethodService)
                .build();
    }

    @Bean
    @Scope("prototype")
    public LiskRestClient liskRestClient() {
        return new LiskRestClientImpl();
    }

    // WAVES-like
    @Bean(name = "wavesServiceImpl")
    public WavesService wavesService() {
        return createWavesService(cryptoCurrencyProperties.getWavesMerchants().getWaves());
    }

//    @Bean(name = "lunesServiceImpl")
//    public WavesService lunesService() {
//        return createWavesService(cryptoCurrencyProperties.getWavesMerchants().getLunes());
//    }

    private WavesService createWavesService(WavesProperty property) {
        return new WavesServiceImpl(property);
    }

    //NEO and Forks
    @Bean(name = "neoServiceImpl")
    public NeoService neoService() {
        Merchant mainMerchant = merchantService.findByName(NeoAsset.NEO.name());
        Currency mainCurrency = currencyService.findByName(NeoAsset.NEO.name());

        Map<String, AssetMerchantCurrencyDto> neoAssetMap = new HashMap<String, AssetMerchantCurrencyDto>() {{
            put(NeoAsset.NEO.getId(), new AssetMerchantCurrencyDto(NeoAsset.NEO, mainMerchant, mainCurrency));
            put(NeoAsset.GAS.getId(), new AssetMerchantCurrencyDto(NeoAsset.GAS, merchantService.findByName(NeoAsset.GAS.name()), currencyService.findByName(NeoAsset.GAS.name())));
        }};
        NeoProperty property = cryptoCurrencyProperties.getNeoMerchants().getNeo().toBuilder()
                .merchant(mainMerchant)
                .currency(mainCurrency)
                .neoAssetMap(neoAssetMap)
                .build();
        return new NeoServiceImpl(property);
    }

    @Bean(name = "kazeServiceImpl")
    public NeoService kazeService() {
        Merchant mainMerchant = merchantService.findByName(NeoAsset.KAZE.name());
        Currency mainCurrency = currencyService.findByName(NeoAsset.KAZE.name());

        Map<String, AssetMerchantCurrencyDto> neoAssetMap = new HashMap<String, AssetMerchantCurrencyDto>() {{
            put(NeoAsset.KAZE.getId(), new AssetMerchantCurrencyDto(NeoAsset.KAZE, mainMerchant, mainCurrency));
            put(NeoAsset.STREAM.getId(), new AssetMerchantCurrencyDto(NeoAsset.STREAM, merchantService.findByName(NeoAsset.STREAM.name()), currencyService.findByName(NeoAsset.STREAM.name())));
        }};
        NeoProperty property = cryptoCurrencyProperties.getNeoMerchants().getNeo().toBuilder()
                .merchant(mainMerchant)
                .currency(mainCurrency)
                .neoAssetMap(neoAssetMap)
                .build();
        return new NeoServiceImpl(property);
    }

    //Qtum tokens:
    @Bean(name = "spcServiceImpl")
    public QtumTokenService spcService() {
        return createQtumService(cryptoCurrencyProperties.getQtumMerchants().getSpc());
    }

    @Bean(name = "hlcServiceImpl")
    public QtumTokenService hlcService() {
        return createQtumService(cryptoCurrencyProperties.getQtumMerchants().getHlc());
    }

    private QtumTokenService createQtumService(QtumProperty property) {
        return new QtumTokenServiceImpl(property);
    }

    //**** Monero ****/
    @Bean(name = "moneroServiceImpl")
    public MoneroService moneroService() {
        return createMoneroService(cryptoCurrencyProperties.getMoneroMerchants().getXmr());
    }

    @Bean(name = "ditcoinServiceImpl")
    public MoneroService ditcoinService() {
        return createMoneroService(cryptoCurrencyProperties.getMoneroMerchants().getDit());
    }

    @Bean(name = "sumoServiceImpl")
    public MoneroService sumoService() {
        return createMoneroService(cryptoCurrencyProperties.getMoneroMerchants().getSumo());
    }

    private MoneroService createMoneroService(MoneroProperty property) {
        return new MoneroServiceImpl(property);
    }

    /***tokens based on xem mosaic)****/
    @Bean(name = "dimCoinServiceImpl")
    public XemMosaicService dimCoinService() {
        return createXemMosaicService(cryptoCurrencyProperties.getXemMerchants().getDim());
    }

    @Bean(name = "npxsServiceImpl")
    public XemMosaicService npxsService() {
        return createXemMosaicService(cryptoCurrencyProperties.getXemMerchants().getNpxs());
    }

    private XemMosaicService createXemMosaicService(XemProperty property) {
        return new XemMosaicServiceImpl(property);
    }

    /***stellarAssets****/
    private @Value("${stellar.slt.emitter}")
    String SLT_EMMITER;

    @Bean(name = "sltStellarService")
    public StellarAsset sltStellarService() {
        return new StellarAsset("SLT",
                "SLT",
                "SLT",
                SLT_EMMITER);
    }

    @Bean(name = "ternStellarService")
    public StellarAsset ternStellarService() {
        return new StellarAsset("TERN",
                "TERN",
                "TERN",
                "GDGQDVO6XPFSY4NMX75A7AOVYCF5JYGW2SHCJJNWCQWIDGOZB53DGP6C");
    }

    @Bean("vexaniumContract")
    public AchainContract achainContractService() {
        return new AchainContract("ACT9XnhX5FtQqGFAa3KgrgkPCCEDPmuzgtSx", "VEX", "VEX", "Vexanium_Token");
    }

    @Bean(name = "vntStellarService")
    public StellarAsset vntStellarService() {
        return new StellarAsset("VNT",
                "VNT",
                "VNT",
                "GC2YBPMNHBHW7R7D2MFRH5RDLC6FGJDCBH7FRSNCHC5326ALOYWGMXLO");
    }

}
