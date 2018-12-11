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
import com.exrates.inout.service.impl.CurrencyServiceImpl;
import com.exrates.inout.service.impl.MerchantServiceImpl;
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

    @Bean(name = "bitcoinCashServiceImpl")
    public BitcoinService bchService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBch());
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

    @Bean(name = "nsrServiceImpl")
    public BitcoinService nsrService() {
        return createBitcoinService(ccp.getBitcoinCoins().getNsr());
    }

    @Bean(name = "amlServiceImpl")
    public BitcoinService amlService() {
        return createBitcoinService(ccp.getBitcoinCoins().getAml());
    }

    @Bean(name = "bbccServiceImpl")
    public BitcoinService bbccService() {
        return createBitcoinService(ccp.getBitcoinCoins().getBbx());
    }

    @Bean(name = "hsrServiceImpl")
    public BitcoinService hcasheService() {
        return createBitcoinService(ccp.getBitcoinCoins().getHsr());
    }

    private BitcoinService createBitcoinService(BitcoinProperty property) {
        return new BitcoinServiceImpl(property);
    }

    //ETH Services
    @Bean(name = "ethereumServiceImpl")
    public EthereumCommonService ethereumService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getEth());
    }

    @Bean(name = "ethereumClassicServiceImpl")
    public EthereumCommonService ethereumClassicService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getEtc());
    }

    @Bean(name = "etzServiceImpl")
    public EthereumCommonService etzService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getEtz());
    }

    @Bean(name = "cloServiceImpl")
    public EthereumCommonService cloService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getClo());
    }

    @Bean(name = "b2gServiceImpl")
    public EthereumCommonService b2gService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getB2g());
    }

    @Bean(name = "golServiceImpl")
    public EthereumCommonService golService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getGol());
    }

    @Bean(name = "cnetServiceImpl")
    public EthereumCommonService cnetService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getCnet());
    }

    @Bean(name = "ntyServiceImpl")
    public EthereumCommonService ntyService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getNty());
    }

    @Bean(name = "etherincServiceImpl")
    public EthereumCommonService etherincService() {
        return createEthereumCommonService(ccp.getEthereumCoins().getEti());
    }

    private EthereumCommonService createEthereumCommonService(EthereumProperty property) {
        return new EthereumCommonServiceImpl(property);
    }

    //Eth tokens
    @Bean(name = "repServiceImpl")
    public EthTokenService RepService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRep());
    }

    @Bean(name = "golemServiceImpl")
    public EthTokenService GolemService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGolem());
    }

    @Bean(name = "omgServiceImpl")
    public EthTokenService OmgService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getOmg());
    }

    @Bean(name = "bnbServiceImpl")
    public EthTokenService BnbService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBnb());
    }

    @Bean(name = "atlServiceImpl")
    public EthTokenService ATLANTService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getAtl());
    }

    @Bean(name = "bitRentServiceImpl")
    public EthTokenService BitRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getRntb());
    }

    @Bean(name = "nioServiceImpl")
    public EthTokenService NioService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNio());
    }

    @Bean(name = "gosServiceImpl")
    public EthTokenService GosService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGos());
    }

    @Bean(name = "bptnServiceImpl")
    public EthTokenService BptnRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBptn());
    }

    @Bean(name = "nbcServiceImpl")
    public EthTokenService NbcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNbc());
    }

    @Bean(name = "taxiServiceImpl")
    public EthTokenService taxiRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTaxi());
    }

    @Bean(name = "nbtkServiceImpl")
    public EthTokenService nbtkRentService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNbtk());
    }

    @Bean(name = "ucashServiceImpl")
    public EthTokenService ucashService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUcash());
    }

    @Bean(name = "nacServiceImpl")
    public EthTokenService nacService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getNac());
    }

    @Bean(name = "echtServiceImpl")
    public EthTokenService echtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getEcht());
    }

    @Bean(name = "idhServiceImpl")
    public EthTokenService idhService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIdh());
    }

    @Bean(name = "cobcServiceImpl")
    public EthTokenService cobcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCobc());
    }

    @Bean(name = "bcsServiceImpl")
    public EthTokenService bcsService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBcs());
    }

    @Bean(name = "uqcServiceImpl")
    public EthTokenService uqcService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getUqc());
    }

    @Bean(name = "inoServiceImpl")
    public EthTokenService inoService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIno());
    }

    @Bean(name = "profitServiceImpl")
    public EthTokenService profitService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getProfit());
    }

    @Bean(name = "ormeServiceImpl")
    public EthTokenService ormeService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getOrme());
    }

    @Bean(name = "bezServiceImpl")
    public EthTokenService bezService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getBez());
    }

    @Bean(name = "simServiceImpl")
    public EthTokenService simService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getSim());
    }

    @Bean(name = "amnServiceImpl")
    public EthTokenService amnService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getAmn());
    }

    @Bean(name = "getServiceImpl")
    public EthTokenService getService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getGet());
    }

    @Bean(name = "flotServiceImpl")
    public EthTokenService flotService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getFlot());
    }

    @Bean(name = "vdgServiceImpl")
    public EthTokenService vdgService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getVdg());
    }

    @Bean(name = "dgtxServiceImpl")
    public EthTokenService dgtxService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getDgtx());
    }

    @Bean(name = "droneServiceImpl")
    public EthTokenService droneService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getDrone());
    }

    @Bean(name = "wdscServiceImpl")
    public EthTokenService wdscService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getWdsc());
    }

    @Bean(name = "fsbtServiceImpl")
    public EthTokenService fsbtService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getFsbt());
    }

    @Bean(name = "iprServiceImpl")
    public EthTokenService iprService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getIpr());
    }

    @Bean(name = "casServiceImpl")
    public EthTokenService casService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getCas());
    }

    @Bean(name = "tnrServiceImpl")
    public EthTokenService tnrService() {
        return createEthereumTokenService(ccp.getEthereumTokenCoins().getTnr());
    }

    private EthTokenService createEthereumTokenService(EthereumTokenProperty property) {
        return new EthTokenServiceImpl(property);
    }

    // LISK-like cryptos
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
    public LiskService arkService() {
        return createLiskService(ccp.getLiskCoins().getArk(), new ArkSpecialMethodServiceImpl(ccp.getLiskCoins().getArk()));
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
        return createWavesService(ccp.getWavesCoins().getWaves());
    }

//    @Bean(name = "lunesServiceImpl")
//    public WavesService lunesService() {
//        return createWavesService(ccp.getWavesCoins().getLunes());
//    }

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

    //Qtum tokens:
    @Bean(name = "spcServiceImpl")
    public QtumTokenServiceImpl spcService() {
        return createQtumService(ccp.getQtumCoins().getSpc());
    }

    @Bean(name = "hlcServiceImpl")
    public QtumTokenServiceImpl hlcService() {
        return createQtumService(ccp.getQtumCoins().getHlc());
    }

    private QtumTokenServiceImpl createQtumService(QtumProperty property) {
        return new QtumTokenServiceImpl(property);
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

    private MoneroService createMoneroService(MoneroProperty property) {
        return new MoneroServiceImpl(property);
    }

    /***tokens based on xem mosaic)****/
    @Bean(name = "dimCoinServiceImpl")
    public XemMosaicService dimCoinService() {
        return createXemMosaicService(ccp.getXemCoins().getDim());
    }

    @Bean(name = "npxsServiceImpl")
    public XemMosaicService npxsService() {
        return createXemMosaicService(ccp.getXemCoins().getNpxs());
    }

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

    private StellarAsset createStellarService(StellarProperty property) {
        return new StellarAsset(property);
    }

    @Bean("vexaniumContract")
    public AchainContract achainContractService() {
        return new AchainContract("ACT9XnhX5FtQqGFAa3KgrgkPCCEDPmuzgtSx", "VEX", "VEX", "Vexanium_Token");
    }
}
