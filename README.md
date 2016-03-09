## 注意：本サンプルはLolipopしかないのAPIを使ってるので、Lolipop以下の端末はうまく動作しない

## SpyFluxExampleサンプルの説明
WordsActivityは、WordsFragment（単語リスト画面）とWordDetailsFragment（単語詳細画面）二つの
fragmentを持っています。
単語リストのアイテムをクリックすると、クリックされた単語の詳細画面が開きます。
詳細画面のボタン押すと、次の単語詳細画面に進みます。
Back key押すと、単語リスト画面に戻ります。

## SpyFluxExample各構成部分の説明（パッケージことに）
（まずSpuFluxというarchitectureを把握した上でこちらの内容を読んだ方がいいと思います）
```
├── actions								// 1
│   ├── ActionDataKey.java				// 2
│   ├── ActionType.java					// 3
│   └── WordsActionCreator.java			// 4
├── model								// 5
│   └── Word.java						// 6
├── stores								// 7
│   └── WordsStore.java					// 8
├── ui									// 9
│   ├── WordDetailsFragment.java		// 10
│   ├── WordRecyclerViewAdapter.java	// 11
│   ├── WordsActivity.java				// 12
│   └── WordsFragment.java				// 13
└── util								// 14
    ├── AnimationUtil.java				// 15
    └── DummyDBHelper.java				// 16

アプリのパッケージ構成は五つの部分を分けるのはオススメです。
1.  actions:
2.  model:
3.  stores: 
9.  ui: Activity, Fragment, Adapterなど、UI関連のものは全部このパッケージに入れます。
14. util: cache, network, db, animationなどの一般的な機能はこのパッケージに入れます。

2.  この中に定義されたkeyは、SpyStoreActionしか使わない（ViewActionにはdata keyがないから）
3.  Actionのタイプはここでまとめられます。StoreActionとViewActionは同じキーをシェアする場合があります。
    基本的に、このclassを見れば、アプリ内とんな操作があるのかすぐわかります。
4.  アプリで使われるデータの生成メソッドと、データ非同期操作メソッドの集合です。
5.  object化されたデータmodelのパッケージです。
8.  単語データに関する全ての操作はここに定義されてます。
10. 単語の詳細画面。ボタン押すたびに、現在勉強している単語の学習回数は+1となり、単語の熟練度も押したボタンによって変わり、さらに次の単語に遷移。
11. UI. ListViewのAdatperです。
12. Fragmentを管理するActivityです。
13. 単語のリスト画面
```

## 新しい機能を追加するなら
### 1. 追加される機能のビジネスロジック・業務フローを理解する
- UI要素図・UI遷移図を描きます。
ビジネスロジック・業務フローというのは、やはり抽象的なもので、
草稿図を借りて、それを具体化にします。
- UIの定義もここでやります：
 * 幾つの画面が必要か
 * それらの画面は完全にActivityことに分けるか、Activity-Fragment Structureを使うか
 * 画面のレイアウト・要素などはもっと具体的に
 * etc...
上のこと決まったら、Activity, Fragmentの実装します。（SpyView interfaceはを各Viewで実現することを忘れずに）

### 2. この業務フローで取り扱うデータの定義
(optional)modelの定義はここでやります。

### 3. データに対する操作の定義
- XXXStoreを定義します
 Storeは、アプリの状態（データ）を保管するデータ倉庫であり、
 データを操作するメソッド（ビジネスロジック）の集合でもあります。
 全てのビジネスロジックはStoreで実装すべきです。
- XXXActionCreatorを定義します
 ActionCreatorは、Storeで使われてるデータの源です。Storeはデータを扱う時に、非同期操作必要になったときに、
 その非同期操作はActionCreatorで実装すべきです。

### 4. ViewActionとStoreActionの定義
業務フローに従って、ViewAction (UI -> Store)と StoreAction(Store -> UI)を定義します。

### その他
cache, network, DB accessなどの非業務の機能は、utilパッケージの中に入れるのはオススメです。