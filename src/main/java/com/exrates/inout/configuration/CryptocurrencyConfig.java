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
import com.exrates.inout.properties.models.StellarProperty;
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
import com.exrates.inout.service.qtum.QtumTokenServiceImpl;
import com.exrates.inout.service.stellar.StellarAsset;
import com.exrates.inout.service.waves.WavesService;
import com.exrates.inout.service.waves.WavesServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBtc());
    }

    @Bean(name = "litecoinServiceImpl")
    public BitcoinService litecoinService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getLtc());
    }

    @Bean(name = "dashServiceImpl")
    public BitcoinService dashService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getDash());
    }

    @Bean(name = "atbServiceImpl")
    public BitcoinService atbService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getAtb());
    }

    @Bean(name = "bitcoinCashServiceImpl")
    public BitcoinService bchService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBch());
    }

    @Bean(name = "dogecoinServiceImpl")
    public BitcoinService dogeService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getDoge());
    }

    @Bean(name = "btgServiceImpl")
    public BitcoinService btgService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBtg());
    }

    @Bean(name = "zcashServiceImpl")
    public BitcoinService zecService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getZec());
    }

    @Bean(name = "b2xServiceImpl")
    public BitcoinService b2xService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getB2x());
    }

    @Bean(name = "bcdServiceImpl")
    public BitcoinService bcdService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBcd());
    }

    @Bean(name = "plcServiceImpl")
    public BitcoinService pbtcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getPlc());
    }

    @Bean(name = "bcxServiceImpl")
    public BitcoinService bcxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBcx());
    }

    @Bean(name = "bciServiceImpl")
    public BitcoinService bciService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBci());
    }

    @Bean(name = "occServiceImpl")
    public BitcoinService occService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getOcc());
    }

    @Bean(name = "btczServiceImpl")
    public BitcoinService btczService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBtcz());
    }

    @Bean(name = "lccServiceImpl")
    public BitcoinService lccService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getLcc());
    }

    @Bean(name = "bitcoinAtomServiceImpl")
    public BitcoinService bitcoinAtomService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBca());
    }

    @Bean(name = "btcpServiceImpl")
    public BitcoinService btcpService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBtcp());
    }

    @Bean(name = "szcServiceImpl")
    public BitcoinService szcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getSzc());
    }

    @Bean(name = "btxServiceImpl")
    public BitcoinService btxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBtx());
    }

    @Bean(name = "bitdollarServiceImpl")
    public BitcoinService bitdollarService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getXbd());
    }

    @Bean(name = "beetServiceImpl")
    public BitcoinService beetService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBeet());
    }

    @Bean(name = "nycoinServiceImpl")
    public BitcoinService nycoinService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getNyc());
    }

    @Bean(name = "ptcServiceImpl")
    public BitcoinService ptcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getPtc());
    }

    @Bean(name = "fgcServiceImpl")
    public BitcoinService fgcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getFgc());
    }

    @Bean(name = "bclServiceImpl")
    public BitcoinService bitcoinCleanService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBcl());
    }

    @Bean(name = "brecoServiceImpl")
    public BitcoinService brecoService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBreco());
    }

    @Bean(name = "ftoServiceImpl")
    public BitcoinService ftoService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getFto());
    }

    @Bean(name = "sabrServiceImpl")
    public BitcoinService sabrService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getSabr());
    }

    @Bean(name = "eqlServiceImpl")
    public BitcoinService eqlService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getEql());
    }

    @Bean(name = "lbtcServiceImpl")
    public BitcoinService lbtcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getLbtc());
    }

    @Bean(name = "brbServiceImpl")
    public BitcoinService brbService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBrb());
    }

    @Bean(name = "rizServiceImpl")
    public BitcoinService rizService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getRiz());
    }

    @Bean(name = "sicServiceImpl")
    public BitcoinService sicService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getSic());
    }

    @Bean(name = "clxServiceImpl")
    public BitcoinService clxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getClx());
    }

    @Bean(name = "qrkServiceImpl")
    public BitcoinService qrkService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getQrk());
    }

    @Bean(name = "cmkServiceImpl")
    public BitcoinService cmkService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getCmk());
    }

    @Bean(name = "mbcServiceImpl")
    public BitcoinService mbcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getMbc());
    }

    @Bean(name = "ddxServiceImpl")
    public BitcoinService ddxService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getDdx());
    }

    @Bean(name = "lpcServiceImpl")
    public BitcoinService lpcService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getLpc());
    }

    @Bean(name = "xfcServiceImpl")
    public BitcoinService xfcServiceImpl() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getXfc());
    }

    @Bean(name = "TOAServiceImpl")
    public BitcoinService taoServiceImpl() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getToa());
    }

    @Bean(name = "crypServiceImpl")
    public BitcoinService crypService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getCryp());
    }

    @Bean(name = "nsrServiceImpl")
    public BitcoinService nsrService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getNsr());
    }

    @Bean(name = "amlServiceImpl")
    public BitcoinService amlService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getAml());
    }

    @Bean(name = "bbccServiceImpl")
    public BitcoinService bbccService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getBbx());
    }

    @Bean(name = "hsrServiceImpl")
    public BitcoinService hcasheService() {
        return createBitcoinService(cryptoCurrencyProperties.getBitcoinCoins().getHsr());
    }

    private BitcoinService createBitcoinService(BitcoinProperty property) {
        return new BitcoinServiceImpl(property);
    }

    //ETH Services
    @Bean(name = "ethereumServiceImpl")
    public EthereumCommonService ethereumService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getEth());
    }

    @Bean(name = "ethereumClassicServiceImpl")
    public EthereumCommonService ethereumClassicService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getEtc());
    }

    @Bean(name = "etzServiceImpl")
    public EthereumCommonService etzService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getEtz());
    }

    @Bean(name = "cloServiceImpl")
    public EthereumCommonService cloService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getClo());
    }

    @Bean(name = "b2gServiceImpl")
    public EthereumCommonService b2gService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getB2g());
    }

    @Bean(name = "golServiceImpl")
    public EthereumCommonService golService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getGol());
    }

    @Bean(name = "cnetServiceImpl")
    public EthereumCommonService cnetService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getCnet());
    }

    @Bean(name = "ntyServiceImpl")
    public EthereumCommonService ntyService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getNty());
    }

    @Bean(name = "etherincServiceImpl")
    public EthereumCommonService etherincService() {
        return createEthereumCommonService(cryptoCurrencyProperties.getEthereumCoins().getEti());
    }

    private EthereumCommonService createEthereumCommonService(EthereumProperty property) {
        return new EthereumCommonServiceImpl(property);
    }

    //Eth tokens
    @Bean(name = "repServiceImpl")
    public EthTokenService RepService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getRep());
    }

    @Bean(name = "golemServiceImpl")
    public EthTokenService GolemService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getGolem());
    }

    @Bean(name = "omgServiceImpl")
    public EthTokenService OmgService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getOmg());
    }

    @Bean(name = "bnbServiceImpl")
    public EthTokenService BnbService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getBnb());
    }

    @Bean(name = "atlServiceImpl")
    public EthTokenService ATLANTService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getAtl());
    }

    @Bean(name = "bitRentServiceImpl")
    public EthTokenService BitRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getRntb());
    }

    @Bean(name = "nioServiceImpl")
    public EthTokenService NioService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getNio());
    }

    @Bean(name = "gosServiceImpl")
    public EthTokenService GosService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getGos());
    }

    @Bean(name = "bptnServiceImpl")
    public EthTokenService BptnRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getBptn());
    }

    @Bean(name = "nbcServiceImpl")
    public EthTokenService NbcService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getNbc());
    }

    @Bean(name = "taxiServiceImpl")
    public EthTokenService taxiRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getTaxi());
    }

    @Bean(name = "nbtkServiceImpl")
    public EthTokenService nbtkRentService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getNbtk());
    }

    @Bean(name = "ucashServiceImpl")
    public EthTokenService ucashService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getUcash());
    }

    @Bean(name = "nacServiceImpl")
    public EthTokenService nacService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getNac());
    }

    @Bean(name = "echtServiceImpl")
    public EthTokenService echtService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getEcht());
    }

    @Bean(name = "idhServiceImpl")
    public EthTokenService idhService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getIdh());
    }

    @Bean(name = "cobcServiceImpl")
    public EthTokenService cobcService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getCobc());
    }

    @Bean(name = "bcsServiceImpl")
    public EthTokenService bcsService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getBcs());
    }

    @Bean(name = "uqcServiceImpl")
    public EthTokenService uqcService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getUqc());
    }

    @Bean(name = "inoServiceImpl")
    public EthTokenService inoService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getIno());
    }

    @Bean(name = "profitServiceImpl")
    public EthTokenService profitService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getProfit());
    }

    @Bean(name = "ormeServiceImpl")
    public EthTokenService ormeService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getOrme());
    }

    @Bean(name = "bezServiceImpl")
    public EthTokenService bezService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getBez());
    }

    @Bean(name = "simServiceImpl")
    public EthTokenService simService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getSim());
    }

    @Bean(name = "amnServiceImpl")
    public EthTokenService amnService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getAmn());
    }

    @Bean(name = "getServiceImpl")
    public EthTokenService getService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getGet());
    }

    @Bean(name = "flotServiceImpl")
    public EthTokenService flotService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getFlot());
    }

    @Bean(name = "vdgServiceImpl")
    public EthTokenService vdgService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getVdg());
    }

    @Bean(name = "dgtxServiceImpl")
    public EthTokenService dgtxService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getDgtx());
    }

    @Bean(name = "droneServiceImpl")
    public EthTokenService droneService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getDrone());
    }

    @Bean(name = "wdscServiceImpl")
    public EthTokenService wdscService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getWdsc());
    }

    @Bean(name = "fsbtServiceImpl")
    public EthTokenService fsbtService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getFsbt());
    }

    @Bean(name = "iprServiceImpl")
    public EthTokenService iprService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getIpr());
    }

    @Bean(name = "casServiceImpl")
    public EthTokenService casService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getCas());
    }

    @Bean(name = "tnrServiceImpl")
    public EthTokenService tnrService() {
        return createEthereumTokenService(cryptoCurrencyProperties.getEthereumTokenCoins().getTnr());
    }

    private EthTokenService createEthereumTokenService(EthereumTokenProperty property) {
        return new EthTokenServiceImpl(property);
    }

    // LISK-like cryptos
    @Bean(name = "liskServiceImpl")
    public LiskService liskService() {
        return createLiskService(cryptoCurrencyProperties.getLiskCoins().getLisk(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "btwServiceImpl")
    public LiskService btwService() {
        return createLiskService(cryptoCurrencyProperties.getLiskCoins().getBtw(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "riseServiceImpl")
    public LiskService riseService() {
        return createLiskService(cryptoCurrencyProperties.getLiskCoins().getRise(), new LiskSpecialMethodServiceImpl(liskRestClient()));
    }

    @Bean(name = "arkServiceImpl")
    public LiskService arkService() {
        return createLiskService(cryptoCurrencyProperties.getLiskCoins().getArk(), new ArkSpecialMethodServiceImpl(cryptoCurrencyProperties.getLiskCoins().getArk()));
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
        return createWavesService(cryptoCurrencyProperties.getWavesCoins().getWaves());
    }

//    @Bean(name = "lunesServiceImpl")
//    public WavesService lunesService() {
//        return createWavesService(cryptoCurrencyProperties.getWavesCoins().getLunes());
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
        NeoProperty property = cryptoCurrencyProperties.getNeoCoins().getNeo().toBuilder()
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
        NeoProperty property = cryptoCurrencyProperties.getNeoCoins().getNeo().toBuilder()
                .merchant(mainMerchant)
                .currency(mainCurrency)
                .neoAssetMap(neoAssetMap)
                .build();
        return new NeoServiceImpl(property);
    }

    //Qtum tokens:
    @Bean(name = "spcServiceImpl")
    public QtumTokenServiceImpl spcService() {
        return createQtumService(cryptoCurrencyProperties.getQtumCoins().getSpc());
    }

    @Bean(name = "hlcServiceImpl")
    public QtumTokenServiceImpl hlcService() {
        return createQtumService(cryptoCurrencyProperties.getQtumCoins().getHlc());
    }

    private QtumTokenServiceImpl createQtumService(QtumProperty property) {
        return new QtumTokenServiceImpl(property);
    }

    //**** Monero ****/
    @Bean(name = "moneroServiceImpl")
    public MoneroService moneroService() {
        return createMoneroService(cryptoCurrencyProperties.getMoneroCoins().getXmr());
    }

    @Bean(name = "ditcoinServiceImpl")
    public MoneroService ditcoinService() {
        return createMoneroService(cryptoCurrencyProperties.getMoneroCoins().getDit());
    }

    @Bean(name = "sumoServiceImpl")
    public MoneroService sumoService() {
        return createMoneroService(cryptoCurrencyProperties.getMoneroCoins().getSumo());
    }

    private MoneroService createMoneroService(MoneroProperty property) {
        return new MoneroServiceImpl(property);
    }

    /***tokens based on xem mosaic)****/
    @Bean(name = "dimCoinServiceImpl")
    public XemMosaicService dimCoinService() {
        return createXemMosaicService(cryptoCurrencyProperties.getXemCoins().getDim());
    }

    @Bean(name = "npxsServiceImpl")
    public XemMosaicService npxsService() {
        return createXemMosaicService(cryptoCurrencyProperties.getXemCoins().getNpxs());
    }

    private XemMosaicService createXemMosaicService(XemProperty property) {
        return new XemMosaicServiceImpl(property);
    }

    @Bean(name = "sltStellarService")
    public StellarAsset sltStellarService() {
        return createStellarService(cryptoCurrencyProperties.getStellarCoins().getSlt());
    }

    @Bean(name = "ternStellarService")
    public StellarAsset ternStellarService() {
        return createStellarService(cryptoCurrencyProperties.getStellarCoins().getTern());
    }

    @Bean(name = "vntStellarService")
    public StellarAsset vntStellarService() {
        return createStellarService(cryptoCurrencyProperties.getStellarCoins().getVnt());
    }

    private StellarAsset createStellarService(StellarProperty property) {
        return new StellarAsset(property);
    }

    @Bean("vexaniumContract")
    public AchainContract achainContractService() {
        return new AchainContract("ACT9XnhX5FtQqGFAa3KgrgkPCCEDPmuzgtSx", "VEX", "VEX", "Vexanium_Token");
    }
}
