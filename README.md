## 注意
本サンプルは5.0以上しかないのAPI（アニメーション周り）を使ってるので、Lolipop以下の端末は動作しないのでご注意ください。
[SpyFlux](https://github.com/Huang-Liangjin/SpyFlux)
の内容を把握してから本exampleを読むのはオススメです。

## SpyFluxExampleサンプルの説明
サンプルの唯一のActivityがあって、WordsActivityです。  
WordsActivityの位置付けはFragmentのContainerです、UIではありません。  
WordsFragment（単語リスト画面）とWordDetailsFragment（単語詳細画面）二つの
fragmentを持っています。  
単語リストのアイテムをクリックすると、クリックされた単語の詳細画面(WrodDetailsFragment)が開きます。  
詳細画面のボタン押すと、次の単語の詳細画面に進みます。  
Back key押すと、単語リスト画面(WordsFragment)に戻ります。

## SpyFluxExample各構成部分の説明
（まずSpuFluxというarchitectureを把握した上でこちらの内容を読んだ方がいいと思います）
```
├── actions                             // 1
│   ├── ActionDataKey.java              // 2
│   ├── ActionType.java                 // 3
│   └── WordsActionCreator.java         // 4
├── model                               // 5
│   └── Word.java                       // 6
├── stores                              // 7
│   └── WordsStore.java                 // 8
├── ui                                  // 9
│   ├── WordDetailsFragment.java        // 10
│   ├── WordRecyclerViewAdapter.java    // 11
│   ├── WordsActivity.java              // 12
│   └── WordsFragment.java              // 13
└── util                                // 14
    ├── AnimationUtil.java              // 15
    └── DummyDBHelper.java              // 16

アプリのパッケージ構成は五つの部分を分けるのはオススメです。
1.  actions: 
    Storeと関わるActionCreatorはこのパッケージに入れます。
5.  model: アプリが使うデータはこのパッケージで定義します。
7.  stores: Storeはこのパッケージで定義します。
9.  ui: Activity, Fragment, Adapterなど、UI関連のものは全部このパッケージに入れます。
14. util: cache, network, db, animationなどの一般的な機能はこのパッケージに入れます。

2.  ActionDataKey: この中に定義されたkeyは、SpyStoreActionしか使わない（ViewActionにはdata keyがないから）
3.  ActionType: ビジネスロジックのタイプを管理しやすため、
    Actionのタイプは全部ここでまとめられます。
    今後、新しい機能が作られ、Actionのタイプを定義する必要がある場合、このTypeの定義をActionTypeクラスに追加してください。
    基本的に、このclassを見れば、アプリはとんな操作があるのかすぐわかります。
4.  ActionCreator: アプリで使われるデータの生成メソッドと、データの非同期操作メソッドの集合です。
6.  Word: サンプルが扱う単語データの定義
8.  WordsStore: 単語データに関する全ての操作はここに定義されてます。
10. WordDetailsFragment: 単語の詳細画面。ボタン押すたびに、現在勉強している単語の学習回数は+1となり、
    単語の熟練度も押したボタンによって変わり、さらに次の単語に遷移。
11. WordRecyclerViewAdapter: 単語リストののAdatperです。
12. WordsActivity: Fragmentを管理するActivityです。
13. WordsFragment: 単語のリスト画面
```

## 新しい機能を追加するなら
### 1. 追加される機能のビジネスロジック・業務フローを理解する
- UI要素図・UI遷移図を描きます。  
ビジネスロジック・業務フローは、やはり抽象的なもので、
草稿図を借りて、それを具体化にします。
- UIの定義もここでやります：
 * 幾つの画面が必要か
 * それらの画面は完全にActivityことに分けるか、Activity-Fragment Structureを使うか
 * 画面のレイアウト・要素などはもっと具体的に
 * etc...

上のこと決まったら、Activity, Fragmentの実装します。（SpyView interfaceはを各Viewで実現することを忘れずに）

### 2. 取り扱うデータの定義
optional. 必要があれば、データのmodelを定義します。

### 3. データに対する操作の定義
- XXXStoreを定義します  
 Storeは、アプリの状態（データ）を保管するデータ倉庫であり、
 データを操作するメソッド（ビジネスロジック）の集合でもあります。
 全てのビジネスロジックはStoreで実装すべきです。
- XXXActionCreatorを定義します(optional)  
 ActionCreatorは、Storeで使われてるデータの源です。また、Storeは非同期操作が必要になったときも、
 その非同期操作はActionCreatorで実装すべきです。

### 4. ViewActionとStoreActionの定義
まずは、ActionTypeクラスでActionのタイプを定義します。
タイプが決まったら、View側で、ユーザのリクエストをカプセルして、SpyViewActionを作ります。
また、Store側で、ユーザに渡すデータをカプセルして、SpyStoreActionを作ります。

### その他
#### 基盤機能について
現在、cache, network, DB accessなどの非業務の機能は、utilパッケージの中に入れてます。
（もっといい管理方法・パッケージ分け方法があるかもしれないですが、現状はutilパッケージにしちゃいます）

#### Storeのsingletonモードについて
Storeはsingletonモードで、一回生成したらずっとメモリに残されてます。一応lazy initialization patternを使って、必要なときしか初期化しないようにしていますが、やはりそのStoreを使い終わったら、なんらかの
メモリ解放手段が必要だと思います。Storeは、一番メモリ食うのはData領域ですので、
Store使い終わったら(dispatcherからunregisterするタイミング)、Data領域クリアしてあげれば、十分だと思います。

