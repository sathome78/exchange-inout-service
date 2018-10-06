package com.exrates.inout.configuration;

import com.exrates.inout.domain.dto.MosaicIdDto;
import com.exrates.inout.service.achain.AchainContract;
import com.exrates.inout.service.ethereum.*;
import com.exrates.inout.service.lisk.*;
import com.exrates.inout.service.merchant.BitcoinService;
import com.exrates.inout.service.merchant.MoneroService;
import com.exrates.inout.service.merchant.impl.BitcoinServiceImpl;
import com.exrates.inout.service.merchant.impl.MoneroServiceImpl;
import com.exrates.inout.service.nem.XemMosaicService;
import com.exrates.inout.service.nem.XemMosaicServiceImpl;
import com.exrates.inout.service.qtum.QtumTokenService;
import com.exrates.inout.service.qtum.QtumTokenServiceImpl;
import com.exrates.inout.service.stellar.StellarAsset;
import com.exrates.inout.service.waves.WavesService;
import com.exrates.inout.service.waves.WavesServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.nem.core.model.primitive.Supply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Log4j2(topic = "config")
@Configuration
public class CryptocurrencyConfig {
    @Bean(name = "bitcoinServiceImpl")
    public BitcoinService bitcoinService() {
        return new BitcoinServiceImpl("merchants/bitcoin_wallet.properties",
                "Bitcoin", "BTC", 4, 15, false);
    }

    @Bean(name = "litecoinServiceImpl")
    public BitcoinService litecoinService() {
        return new BitcoinServiceImpl("merchants/litecoin_wallet.properties",
                "Litecoin", "LTC", 4, 20, false);
    }

    @Bean(name = "dashServiceImpl")
    public BitcoinService dashService() {
        return new BitcoinServiceImpl("merchants/dash_wallet.properties",
                "Dash", "DASH", 4, 20, false);
    }

    @Bean(name = "atbServiceImpl")
    public BitcoinService atbService() {
        return new BitcoinServiceImpl("merchants/atb_wallet.properties",
                "ATB", "ATB", 10, 20, false);
    }

    @Bean(name = "bitcoinCashServiceImpl")
    public BitcoinService bchService() {
        return new BitcoinServiceImpl("merchants/bitcoin_cash_wallet.properties",
                "Bitcoin Cash", "BCH", 4, 20, false);
    }

    @Bean(name = "dogecoinServiceImpl")
    public BitcoinService dogeService() {
        return new BitcoinServiceImpl("merchants/dogecoin_wallet.properties",
                "Dogecoin", "DOGE", 4, 20, false);
    }

    @Bean(name = "btgServiceImpl")
    public BitcoinService btgService() {
        return new BitcoinServiceImpl("merchants/bitcoin_gold_wallet.properties",
                "BTG", "BTG", 4, 20, false);
    }

    @Bean(name = "zcashServiceImpl")
    public BitcoinService zecService() {
        return new BitcoinServiceImpl("merchants/zec_wallet.properties",
                "Zcash", "ZEC", 4, 20, false);
    }

    @Bean(name = "b2xServiceImpl")
    public BitcoinService b2xService() {
        return new BitcoinServiceImpl("merchants/b2x_wallet.properties",
                "B2X", "B2X", 4, 20, false);
    }

    @Bean(name = "bcdServiceImpl")
    public BitcoinService bcdService() {
        return new BitcoinServiceImpl("merchants/bcd_wallet.properties",
                "BCD", "BCD", 4, 20, false);
    }

    @Bean(name = "plcServiceImpl")
    public BitcoinService pbtcService() {
        return new BitcoinServiceImpl("merchants/plc_wallet.properties",
                "PLC", "PLC", 4, 20, false);
    }

    @Bean(name = "bcxServiceImpl")
    public BitcoinService bcxService() {
        return new BitcoinServiceImpl("merchants/bcx_wallet.properties",
                "BCX", "BCX", 4, 20, false);
    }

    @Bean(name = "bciServiceImpl")
    public BitcoinService bciService() {
        return new BitcoinServiceImpl("merchants/bci_wallet.properties",
                "BCI", "BCI", 4, 20, false);
    }

    @Bean(name = "occServiceImpl")
    public BitcoinService occService() {
        return new BitcoinServiceImpl("merchants/occ_wallet.properties",
                "OCC", "OCC", 4, 20, false);
    }

    @Bean(name = "btczServiceImpl")
    public BitcoinService btczService() {
        return new BitcoinServiceImpl("merchants/btcz_wallet.properties",
                "BTCZ", "BTCZ", 4, 20, false);
    }

    @Bean(name = "lccServiceImpl")
    public BitcoinService lccService() {
        return new BitcoinServiceImpl("merchants/lcc_wallet.properties",
                "LCC", "LCC", 4, 20, false);
    }

    @Bean(name = "bitcoinAtomServiceImpl")
    public BitcoinService bitcoinAtomService() {
        return new BitcoinServiceImpl("merchants/bca_wallet.properties",
                "BitcoinAtom", "BCA", 4, 20, false);
    }

    @Bean(name = "btcpServiceImpl")
    public BitcoinService btcpService() {
        return new BitcoinServiceImpl("merchants/btcp_wallet.properties",
                "BTCP", "BTCP", 4, 20, false);
    }

    @Bean(name = "szcServiceImpl")
    public BitcoinService szcService() {
        return new BitcoinServiceImpl("merchants/szc_wallet.properties",
                "SZC", "SZC", 4, 20, false, false);
    }

    @Bean(name = "btxServiceImpl")
    public BitcoinService btxService() {
        return new BitcoinServiceImpl("merchants/btx_wallet.properties",
                "BTX", "BTX", 4, 20, false, false);
    }

    @Bean(name = "bitdollarServiceImpl")
    public BitcoinService bitdollarService() {
        return new BitcoinServiceImpl("merchants/xbd_wallet.properties",
                "BitDollar", "XBD", 4, 20, false, false);
    }

    @Bean(name = "beetServiceImpl")
    public BitcoinService beetService() {
        return new BitcoinServiceImpl("merchants/beet_wallet.properties",
                "BEET", "BEET", 4, 20, false, false);
    }

    @Bean(name = "nycoinServiceImpl")
    public BitcoinService nycoinService() {
        return new BitcoinServiceImpl("merchants/nyc_wallet.properties",
                "NYC", "NYC", 4, 20, false, false);
    }

    @Bean(name = "ptcServiceImpl")
    public BitcoinService ptcService() {
        return new BitcoinServiceImpl("merchants/perfectcoin_wallet.properties",
                "Perfectcoin", "PTC", 4, 20, false, false);
    }

    @Bean(name = "fgcServiceImpl")
    public BitcoinService fgcService() {
        return new BitcoinServiceImpl("merchants/fgc_wallet.properties",
                "FGC", "FGC", 4, 20, false, false);
    }

    @Bean(name = "bclServiceImpl")
    public BitcoinService bitcoinCleanService() {
        return new BitcoinServiceImpl("merchants/bcl_wallet.properties",
                "BitcoinClean", "BCL", 4, 20, false);
    }

    @Bean(name = "brecoServiceImpl")
    public BitcoinService brecoService() {
        return new BitcoinServiceImpl("merchants/breco_wallet.properties",
                "BRECO", "BRECO", 4, 20, false,
                false, true, true);
    }

    @Bean(name = "ftoServiceImpl")
    public BitcoinService ftoService() {
        return new BitcoinServiceImpl("merchants/fto_wallet.properties",
                "FTO", "FTO", 4, 20, false, false);
    }

    @Bean(name = "sabrServiceImpl")
    public BitcoinService sabrService() {
        return new BitcoinServiceImpl("merchants/sabr_wallet.properties",
                "SABR", "SABR", 4, 20, false, false);
    }

    @Bean(name = "eqlServiceImpl")
    public BitcoinService eqlService() {
        return new BitcoinServiceImpl("merchants/eql_wallet.properties",
                "EQL", "EQL", 4, 20, false);
    }

    @Bean(name = "lbtcServiceImpl")
    public BitcoinService lbtcService() {
        return new BitcoinServiceImpl("merchants/lbtc_wallet.properties",
                "LBTC", "LBTC", 4, 20, false);
    }

    @Bean(name = "brbServiceImpl")
    public BitcoinService brbService() {
        return new BitcoinServiceImpl("merchants/brb_wallet.properties",
                "BRB", "BRB", 4, 20, false, false);
    }

    @Bean(name = "rizServiceImpl")
    public BitcoinService rizService() {
        return new BitcoinServiceImpl("merchants/riz_wallet.properties",
                "RIZ", "RIZ", 4, 20, false);
    }

    @Bean(name = "sicServiceImpl")
    public BitcoinService sicService() {
        return new BitcoinServiceImpl("merchants/sic_wallet.properties", "SIC", "SIC", 4, 20, false, false);
    }

    @Bean(name = "clxServiceImpl")
    public BitcoinService clxService() {
        return new BitcoinServiceImpl("merchants/clx_wallet.properties",
                "CLX", "CLX", 4, 20, false, false);
    }

    // LISK-like cryptos


    @Bean(name = "liskServiceImpl")
    public LiskService liskService() {
        LiskRestClient restClient = liskRestClient();
        return new LiskServiceImpl(restClient, new LiskSpecialMethodServiceImpl(restClient),
                "Lisk", "LSK", "merchants/lisk.properties");
    }

    @Bean(name = "btwServiceImpl")
    public LiskService btwService() {
        LiskRestClient restClient = liskRestClient();
        return new LiskServiceImpl(restClient, new LiskSpecialMethodServiceImpl(restClient), "BitcoinWhite", "BTW", "merchants/bitcoin_white.properties");
    }

    @Bean(name = "riseServiceImpl")
    public LiskService riseService() {
        LiskRestClient restClient = liskRestClient();
        return new LiskServiceImpl(restClient, new LiskSpecialMethodServiceImpl(restClient),
                "RiseVision", "RISE", "merchants/rise_vision.properties");
    }

    @Bean(name = "arkServiceImpl")
    public LiskService arkService() {
        return new LiskServiceImpl(liskRestClient(), arkSendTxService(), "Ark", "ARK", "merchants/ark.properties");
    }

    @Bean
    @Scope("prototype")
    public LiskRestClient liskRestClient() {
        return new LiskRestClientImpl();
    }

    @Bean
    @Scope("prototype")
    public LiskSpecialMethodService arkSendTxService() {
        return new ArkSpecialMethodServiceImpl("merchants/ark.properties");
    }


    // WAVES-like

    @Bean(name = "wavesServiceImpl")
    public WavesService wavesService() {
        return new WavesServiceImpl("WAVES", "Waves", "merchants/waves.properties");
    }

    @Bean(name = "lunesServiceImpl")
    public WavesService lunesService() {
        return new WavesServiceImpl("LUNES", "LUNES", "merchants/lunes.properties");
    }


    @Bean(name = "nsrServiceImpl")
    public BitcoinService nsrService() {
        return new BitcoinServiceImpl("merchants/nushares_wallet.properties",
                "NuShares", "NSR", 4, 20, false, false);
    }

    @Bean(name = "amlServiceImpl")
    public BitcoinService amlService() {
        return new BitcoinServiceImpl("merchants/aml_wallet.properties",
                "AML", "ABTC", 4, 20, false);
    }

    @Bean(name = "bbccServiceImpl")
    public BitcoinService bbccService() {
        return new BitcoinServiceImpl("merchants/bbcc_wallet.properties",
                "BBX", "BBX", 4, 20, false, false, false);
    }

    @Bean(name = "hsrServiceImpl")
    public BitcoinService hcasheService() {
        return new BitcoinServiceImpl("merchants/hsr_wallet.properties",
                "HSR", "HSR", 4, 20, false, false);
    }

    @Bean(name = "ethereumServiceImpl")
    public EthereumCommonService ethereumService() {
        return new EthereumCommonServiceImpl("merchants/ethereum.properties",
                "Ethereum", "ETH", 12);
    }

    @Bean(name = "ethereumClassicServiceImpl")
    public EthereumCommonService ethereumClassicService() {
        return new EthereumCommonServiceImpl("merchants/ethereumClassic.properties",
                "Ethereum Classic", "ETC", 12);
    }

    @Bean(name = "etzServiceImpl")
    public EthereumCommonService etzService() {
        return new EthereumCommonServiceImpl("merchants/etherzero.properties",
                "EtherZero", "ETZ", 12);
    }

    @Bean(name = "cloServiceImpl")
    public EthereumCommonService cloService() {
        return new EthereumCommonServiceImpl("merchants/callisto.properties",
                "CLO", "CLO", 12);
    }

    @Bean(name = "b2gServiceImpl")
    public EthereumCommonService b2gService() {
        return new EthereumCommonServiceImpl("merchants/bitcoiin2g.properties",
                "B2G", "B2G", 12);
    }

    @Bean(name = "golServiceImpl")
    public EthereumCommonService golService() {
        return new EthereumCommonServiceImpl("merchants/goldiam.properties",
                "GOL", "GOL", 12);
    }

    @Bean(name = "cnetServiceImpl")
    public EthereumCommonService cnetService() {
        return new EthereumCommonServiceImpl("merchants/contractnet.properties",
                "CNET", "CNET", 0);
    }

    @Bean(name = "ntyServiceImpl")
    public EthereumCommonService ntyService() {
        return new EthereumCommonServiceImpl("merchants/nexty.properties",
                "NTY", "NTY", 12);
    }

    @Bean(name = "etherincServiceImpl")
    public EthereumCommonService etherincService() {
        return new EthereumCommonServiceImpl("merchants/eti.properties",
                "ETI", "ETI", 12);
    }

    @Bean(name = "eosServiceImpl")
    public EthTokenService EosService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0");
        return new EthTokenServiceImpl(
                tokensList,
                "EOS",
                "EOS", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "repServiceImpl")
    public EthTokenService RepService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xe94327d07fc17907b4db788e5adf2ed424addff6");
        return new EthTokenServiceImpl(
                tokensList,
                "REP",
                "REP", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "golemServiceImpl")
    public EthTokenService GolemService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xa74476443119a942de498590fe1f2454d7d4ac0d");
        return new EthTokenServiceImpl(
                tokensList,
                "Golem",
                "GNT", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "omgServiceImpl")
    public EthTokenService OmgService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xd26114cd6ee289accf82350c8d8487fedb8a0c07");
        return new EthTokenServiceImpl(
                tokensList,
                "OmiseGo",
                "OMG", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "bnbServiceImpl")
    public EthTokenService BnbService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xb8c77482e45f1f44de1745f52c74426c631bdd52");
        return new EthTokenServiceImpl(
                tokensList,
                "BinanceCoin",
                "BNB", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "atlServiceImpl")
    public EthTokenService ATLANTService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x78b7fada55a64dd895d8c8c35779dd8b67fa8a05");
        return new EthTokenServiceImpl(
                tokensList,
                "ATLANT",
                "ATL", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "bitRentServiceImpl")
    public EthTokenService BitRentService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x1fe70be734e473e5721ea57c8b5b01e6caa52686");
        return new EthTokenServiceImpl(
                tokensList,
                "BitRent",
                "RNTB", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "nioServiceImpl")
    public EthTokenService NioService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x5554e04e76533e1d14c52f05beef6c9d329e1e30");
        return new EthTokenServiceImpl(
                tokensList,
                "NIO",
                "NIO", true, ExConvert.Unit.WEI);
    }

    @Bean(name = "gosServiceImpl")
    public EthTokenService GosService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x5ce8e61f28f5948de4913bcaada90039481f1f53");
        return new EthTokenServiceImpl(
                tokensList,
                "GOS",
                "GOS", true, ExConvert.Unit.MWEI);
    }


    @Bean(name = "bptnServiceImpl")
    public EthTokenService BptnRentService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x6c22b815904165f3599f0a4a092d458966bd8024");
        return new EthTokenServiceImpl(
                tokensList,
                "BPTN",
                "BPTN", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "nbcServiceImpl")
    public EthTokenService NbcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x9f195617fa8fbad9540c5d113a99a0a0172aaedc");
        return new EthTokenServiceImpl(
                tokensList,
                "NBC",
                "NBC", true, ExConvert.Unit.ETHER);
    }


    @Bean(name = "taxiServiceImpl")
    public EthTokenService taxiRentService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x8409e9c7d23ae978e809866abf46ac2e116f4d0e");
        return new EthTokenServiceImpl(
                tokensList,
                "TAXI",
                "TAXI", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "nbtkServiceImpl")
    public EthTokenService nbtkRentService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xb0e6f83eba6a4ea20617e134b1aee30fcb0ac634");
        return new EthTokenServiceImpl(
                tokensList,
                "NBTK",
                "NBTK", false, ExConvert.Unit.WEI);
    }

    @Bean(name = "ucashServiceImpl")
    public EthTokenService ucashService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x92e52a1a235d9a103d970901066ce910aacefd37");
        return new EthTokenServiceImpl(
                tokensList,
                "UCASH",
                "UCASH", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "nacServiceImpl")
    public EthTokenService nacService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x8d80de8a78198396329dfa769ad54d24bf90e7aa");
        return new EthTokenServiceImpl(
                tokensList,
                "NAC",
                "NAC", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "echtServiceImpl")
    public EthTokenService echtService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x1a2277c83930b7a64c3e3d5544eaa8c4f946b1b7");
        return new EthTokenServiceImpl(
                tokensList,
                "ECHT",
                "ECHT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "idhServiceImpl")
    public EthTokenService idhService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x5136c98a80811c3f46bdda8b5c4555cfd9f812f0");
        return new EthTokenServiceImpl(
                tokensList,
                "IDH",
                "IDH", false, ExConvert.Unit.MWEI);
    }

    @Bean(name = "cobcServiceImpl")
    public EthTokenService cobcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x6292cec07c345c6c6953e9166324f58db6d9f814");
        return new EthTokenServiceImpl(
                tokensList,
                "COBC",
                "COBC", true, ExConvert.Unit.ETHER);
    }


    @Bean(name = "bcsServiceImpl")
    public EthTokenService bcsService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x98bde3a768401260e7025faf9947ef1b81295519");
        return new EthTokenServiceImpl(
                tokensList,
                "BCS",
                "BCS", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "uqcServiceImpl")
    public EthTokenService uqcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xd01db73e047855efb414e6202098c4be4cd2423b");
        return new EthTokenServiceImpl(
                tokensList,
                "UQC",
                "UQC", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "inoServiceImpl")
    public EthTokenService inoService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xc9859fccc876e6b4b3c749c5d29ea04f48acb74f");
        return new EthTokenServiceImpl(
                tokensList,
                "INO",
                "INO", true, ExConvert.Unit.WEI);
    }

    @Bean(name = "profitServiceImpl")
    public EthTokenService profitService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xe540935cabf4c2bac547c8067cbbc2991d030122");
        return new EthTokenServiceImpl(
                tokensList,
                "PROFIT",
                "PROFIT", false, ExConvert.Unit.ETHER);
    }


    @Bean(name = "ormeServiceImpl")
    public EthTokenService ormeService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x516e5436bafdc11083654de7bb9b95382d08d5de");
        return new EthTokenServiceImpl(
                tokensList,
                "ORME",
                "ORME", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "bezServiceImpl")
    public EthTokenService bezService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x3839d8ba312751aa0248fed6a8bacb84308e20ed");
        return new EthTokenServiceImpl(
                tokensList,
                "BEZ",
                "BEZ", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "simServiceImpl")
    public EthTokenService simService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x7528e3040376edd5db8263db2f5bd1bed91467fb");
        return new EthTokenServiceImpl(
                tokensList,
                "SIM",
                "SIM", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "amnServiceImpl")
    public EthTokenService amnService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x737f98ac8ca59f2c68ad658e3c3d8c8963e40a4c");
        return new EthTokenServiceImpl(
                tokensList,
                "AMN",
                "AMN", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "getServiceImpl")
    public EthTokenService getService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x8a854288a5976036a725879164ca3e91d30c6a1b");
        return new EthTokenServiceImpl(
                tokensList,
                "GET",
                "GET", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "flotServiceImpl")
    public EthTokenService flotService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x049399a6b048d52971f7d122ae21a1532722285f");
        return new EthTokenServiceImpl(
                tokensList,
                "FLOT",
                "FLOT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "vdgServiceImpl")
    public EthTokenService vdgService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x57c75eccc8557136d32619a191fbcdc88560d711");
        return new EthTokenServiceImpl(
                tokensList,
                "VDG",
                "VDG", true, ExConvert.Unit.WEI);
    }

    @Bean(name = "dgtxServiceImpl")
    public EthTokenService dgtxService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x1c83501478f1320977047008496dacbd60bb15ef");
        return new EthTokenServiceImpl(
                tokensList,
                "DGTX",
                "DGTX", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "droneServiceImpl")
    public EthTokenService droneService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x131f193692b5cce8c87d12ff4f7aa1d4e1668f1e");
        return new EthTokenServiceImpl(
                tokensList,
                "DRONE",
                "DRONE", true, ExConvert.Unit.WEI);
    }

    @Bean(name = "wdscServiceImpl")
    public EthTokenService wdscService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x170cf89358ce17742955ea43927da5fc1e8e1211");
        return new EthTokenServiceImpl(
                tokensList,
                "WDSC",
                "WDSC", true, ExConvert.Unit.ETHER, new BigInteger("1"));
    }

    @Bean(name = "fsbtServiceImpl")
    public EthTokenService fsbtService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x1ed7ae1f0e2fa4276dd7ddc786334a3df81d50c0");
        return new EthTokenServiceImpl(
                tokensList,
                "FSBT",
                "FSBT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "iprServiceImpl")
    public EthTokenService iprService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x069bc4608a8764924ab991cb9eb6d6b6caad74c8");
        return new EthTokenServiceImpl(
                tokensList,
                "IPR",
                "IPR", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "casServiceImpl")
    public EthTokenService casService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xe8780b48bdb05f928697a5e8155f672ed91462f7");
        return new EthTokenServiceImpl(
                tokensList,
                "CAS",
                "CAS", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "tnrServiceImpl")
    public EthTokenService tnrService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x543199bfd8c343fadd8c1a2bc289e876c588c8e5");
        return new EthTokenServiceImpl(
                tokensList,
                "TNR",
                "TNR", true, ExConvert.Unit.ETHER);
    }

    //    Qtum tokens:
    @Bean(name = "inkServiceImpl")
    public EthTokenService InkService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xf4c90e18727c5c76499ea6369c856a6d61d3e92e");
        return new EthTokenServiceImpl(
                tokensList,
                "Ink",
                "INK", true, ExConvert.Unit.GWEI);
    }

    @Bean(name = "rthServiceImpl")
    public EthTokenService rthService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x3fd8f39a962efda04956981c31ab89fab5fb8bc8");
        return new EthTokenServiceImpl(
                tokensList,
                "RTH",
                "RTH", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "spdServiceImpl")
    public EthTokenService SpdService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x1dea979ae76f26071870f824088da78979eb91c8");
        return new EthTokenServiceImpl(
                tokensList,
                "SPD",
                "SPD", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "mtcServiceImpl")
    public EthTokenService MtcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x905e337c6c8645263d3521205aa37bf4d034e745");
        return new EthTokenServiceImpl(
                tokensList,
                "MTC",
                "MTC", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "arnServiceImpl")
    public EthTokenService arnService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xba5f11b16b155792cf3b2e6880e8706859a8aeb6");
        return new EthTokenServiceImpl(
                tokensList,
                "ARN",
                "ARN", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "hstServiceImpl")
    public EthTokenService hstService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x554c20b7c486beee439277b4540a434566dc4c02");
        return new EthTokenServiceImpl(
                tokensList,
                "HST",
                "HST", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "dtrcServiceImpl")
    public EthTokenService DtrcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xc20464e0c373486d2b3335576e83a218b1618a5e");
        return new EthTokenServiceImpl(
                tokensList,
                "DTRC",
                "DTRC", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "ceekServiceImpl")
    public EthTokenService CeekService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xb056c38f6b7dc4064367403e26424cd2c60655e1");
        return new EthTokenServiceImpl(
                tokensList,
                "CEEK",
                "CEEK", false, ExConvert.Unit.ETHER);
    }


    @Bean(name = "anyServiceImpl")
    public EthTokenService anyService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xdf67cf04f1f268e431bfecf2c76843afb8e536c1");
        return new EthTokenServiceImpl(
                tokensList,
                "ANY",
                "ANY", false, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "tgameServiceImpl")
    public EthTokenService tgameService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xf8e06e4e4a80287fdca5b02dccecaa9d0954840f");
        return new EthTokenServiceImpl(
                tokensList,
                "TGAME",
                "TGAME", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "mtlServiceImpl")
    public EthTokenService mtlServiceImpl() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xf433089366899d83a9f26a773d59ec7ecf30355e");
        return new EthTokenServiceImpl(
                tokensList,
                "MTL",
                "MTL", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "leduServiceImpl")
    public EthTokenService leduService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x5b26c5d0772e5bbac8b3182ae9a13f9bb2d03765");
        return new EthTokenServiceImpl(
                tokensList,
                "LEDU",
                "LEDU", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "adbServiceImpl")
    public EthTokenService adbService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x2baac9330cf9ac479d819195794d79ad0c7616e3");
        return new EthTokenServiceImpl(
                tokensList,
                "ADB",
                "ADB", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "cedexServiceImpl")
    public EthTokenService cedexService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xf4065e4477e91c177ded71a7a6fb5ee07dc46bc9");
        return new EthTokenServiceImpl(
                tokensList,
                "CEDEX",
                "CEDEX", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "gstServiceImpl")
    public EthTokenService gstService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x67a9099f0008c35c61c00042cd9fb03684451097");
        return new EthTokenServiceImpl(
                tokensList,
                "GST",
                "GST", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "satServiceImpl")
    public EthTokenService satService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xc56b13ebbcffa67cfb7979b900b736b3fb480d78");
        return new EthTokenServiceImpl(
                tokensList,
                "SAT",
                "SAT", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "cheServiceImpl")
    public EthTokenService cheService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x632f62fcf63cb56380ffd27d63afcf5f1349f73f");
        return new EthTokenServiceImpl(
                tokensList,
                "CHE",
                "CHE", false, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "daccServiceImpl")
    public EthTokenService daccService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xf8c595d070d104377f58715ce2e6c93e49a87f3c");
        return new EthTokenServiceImpl(
                tokensList,
                "DACC",
                "DACC", true, ExConvert.Unit.MWEI);
    }

    @Bean(name = "engtServiceImpl")
    public EthTokenService engtService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x5dbac24e98e2a4f43adc0dc82af403fca063ce2c");
        return new EthTokenServiceImpl(
                tokensList,
                "ENGT",
                "ENGT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "tavittServiceImpl")
    public EthTokenService tavittService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xdd690d8824c00c84d64606ffb12640e932c1af56");
        return new EthTokenServiceImpl(
                tokensList,
                "TAVITT",
                "TAVITT", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "umtServiceImpl")
    public EthTokenService umtService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xc6be00f7ed386015a3c751d38c126c62f231138d");
        return new EthTokenServiceImpl(
                tokensList,
                "UMT",
                "UMT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "maspServiceImpl")
    public EthTokenService maspService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xce958ecf2c752c74973e89674faa30404b15a498");
        return new EthTokenServiceImpl(
                tokensList,
                "MASP",
                "MASP", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "skillServiceImpl")
    public EthTokenService skillService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x417d6feeae8b2fcb73d14d64be7f192e49431978");
        return new EthTokenServiceImpl(
                tokensList,
                "SKILL",
                "SKILL", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "storServiceImpl")
    public EthTokenService storService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xa3ceac0aac5c5d868973e546ce4731ba90e873c2");
        return new EthTokenServiceImpl(
                tokensList,
                "STOR",
                "STOR", true, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "quintServiceImpl")
    public EthTokenService quintService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x45b73654e464945a268032cdcb8d551fe8b733ca");
        return new EthTokenServiceImpl(
                tokensList,
                "QUiNT",
                "QUiNT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "ttcServiceImpl")
    public EthTokenService ttcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x53e28b07e0795869b727ee4d585b3c025b016952");
        return new EthTokenServiceImpl(
                tokensList,
                "TTC",
                "TTC", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "bfgServiceImpl")
    public EthTokenService bfgService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x2ee3b3804f695355ddc4f8e1c54654416d7ee95a");
        return new EthTokenServiceImpl(
                tokensList,
                "BFG",
                "BFG", false, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "jetServiceImpl")
    public EthTokenService jetService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x8727c112c712c4a03371ac87a74dd6ab104af768");
        return new EthTokenServiceImpl(
                tokensList,
                "JET",
                "JET", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "patServiceImpl")
    public EthTokenService patService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xf3b3cad094b89392fce5fafd40bc03b80f2bc624");
        return new EthTokenServiceImpl(
                tokensList,
                "PAT",
                "PAT", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "mtvServiceImpl")
    public EthTokenService mtvService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x07a7ed332c595b53a317afcee50733af571475e7");
        return new EthTokenServiceImpl(
                tokensList,
                "eMTV",
                "eMTV", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "kwattServiceImpl")
    public EthTokenService kwattService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x241ba672574a78a3a604cdd0a94429a73a84a324");
        return new EthTokenServiceImpl(
                tokensList,
                "KWATT",
                "KWATT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "tusdServiceImpl")
    public EthTokenService tusdService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x8dd5fbce2f6a956c3022ba3663759011dd51e73e");
        return new EthTokenServiceImpl(
                tokensList,
                "TUSD",
                "TUSD", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "fpwrServiceImpl")
    public EthTokenService fpwrService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xdd92e60563250012ee1c4cb4b26810c45a0591da");
        return new EthTokenServiceImpl(
                tokensList,
                "FPWR",
                "FPWR", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "crbtServiceImpl")
    public EthTokenService crbtService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x2cf618c19041d9db330d8222b860a624021f30fb");
        return new EthTokenServiceImpl(
                tokensList,
                "CRBT",
                "CRBT", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "hiveServiceImpl")
    public EthTokenService hiveService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x895f5d0b8456b980786656a33f21642807d1471c");
        return new EthTokenServiceImpl(
                tokensList,
                "HIVE",
                "HIVE", false, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "cmitServiceImpl")
    public EthTokenService cmitService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xe11609b9a51caf7d32a55896386ac52ed90e66f1");
        return new EthTokenServiceImpl(
                tokensList,
                "CMIT",
                "CMIT", false, ExConvert.Unit.AIWEI);
    }

    @Bean(name = "hdrServiceImpl")
    public EthTokenService hdrService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x52494fbffe10f8c29411521040ae8618c334981e");
        return new EthTokenServiceImpl(
                tokensList,
                "HDR",
                "HDR", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "racServiceImpl")
    public EthTokenService racService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x342ba159f988f24f0b033f3cc5232377ee500543");
        return new EthTokenServiceImpl(
                tokensList,
                "RAC",
                "RAC", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "iqnServiceImpl")
    public EthTokenService iqnService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x0db8d8b76bc361bacbb72e2c491e06085a97ab31");
        return new EthTokenServiceImpl(
                tokensList,
                "IQN",
                "IQN", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "gexServiceImpl")
    public EthTokenService gexService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xdac15794f0fadfdcf3a93aeaabdc7cac19066724");
        return new EthTokenServiceImpl(
                tokensList,
                "GEX",
                "GEX", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "ixeServiceImpl")
    public EthTokenService ixeService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x7a07e1a0c2514d51132183ecfea2a880ec3b7648");
        return new EthTokenServiceImpl(
                tokensList,
                "IXE",
                "IXE", false, ExConvert.Unit.ETHER);
    }

    @Bean(name = "nerServiceImpl")
    public EthTokenService nerService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0xee5dfb5ddd54ea2fb93b796a8a1b83c3fe38e0e6");
        return new EthTokenServiceImpl(
                tokensList,
                "NER",
                "NER", true, com.exrates.inout.service.ethereum.ExConvert.Unit.ETHER);
    }

    @Bean(name = "phiServiceImpl")
    public EthTokenService phiService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x13c2fab6354d3790d8ece4f0f1a3280b4a25ad96");
        return new EthTokenServiceImpl(
                tokensList,
                "PHI",
                "PHI", true, ExConvert.Unit.ETHER);
    }

    @Bean(name = "mftuServiceImpl")
    public EthTokenService mftuService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("0x05d412ce18f24040bb3fa45cf2c69e506586d8e8");
        return new EthTokenServiceImpl(
                tokensList,
                "MFTU",
                "MFTU", true, ExConvert.Unit.ETHER);
    }

    //    Qtum tokens:
    @Bean(name = "spcServiceImpl")
    public QtumTokenService spcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("57931faffdec114056a49adfcaa1caac159a1a25");

        return new QtumTokenServiceImpl(tokensList, "SPC", "SPC", com.exrates.inout.util.ExConvert.Unit.AIWEI);
    }

    @Bean(name = "hlcServiceImpl")
    public QtumTokenService hlcService() {
        List<String> tokensList = new ArrayList<>();
        tokensList.add("b27d7bf95b03e02b55d5eb63d3f1692762101bf9");

        return new QtumTokenServiceImpl(tokensList, "HLC", "HLC", com.exrates.inout.util.ExConvert.Unit.GWEI);
    }

    //**** Monero ****/
    @Bean(name = "moneroServiceImpl")
    public MoneroService moneroService() {
        return new MoneroServiceImpl("merchants/monero.properties",
                "Monero", "XMR", 10, 12);
    }

    @Bean(name = "ditcoinServiceImpl")
    public MoneroService ditcoinService() {
        return new MoneroServiceImpl("merchants/ditcoin.properties",
                "DIT", "DIT", 10, 8);
    }

    @Bean(name = "sumoServiceImpl")
    public MoneroService sumoService() {
        return new MoneroServiceImpl("merchants/sumokoin.properties",
                "SUMO", "SUMO", 10, 9);
    }

    /***tokens based on xem mosaic)****/
    @Bean(name = "dimCoinServiceImpl")
    public XemMosaicService dimCoinService() {
        return new XemMosaicServiceImpl(
                "DimCoin",
                "DIM",
                new MosaicIdDto("dim", "coin"),
                1000000,
                6,
                new Supply(9000000000L),
                10);
    }


    @Bean(name = "npxsServiceImpl")
    public XemMosaicService npxsService() {
        return new XemMosaicServiceImpl(
                "NPXSXEM",
                "NPXSXEM",
                new MosaicIdDto("pundix", "npxs"),
                1000000,
                6,
                new Supply(9000000000L),
                0);
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
