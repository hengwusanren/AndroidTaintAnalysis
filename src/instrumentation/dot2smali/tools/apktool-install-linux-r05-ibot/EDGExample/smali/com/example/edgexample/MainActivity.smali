.class public Lcom/example/edgexample/MainActivity;
.super Landroid/app/Activity;
.source "MainActivity.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# static fields
.field private static final TAG:Ljava/lang/String; = "MainActivity"


# instance fields
.field button1:Landroid/widget/Button;

.field button2:Landroid/widget/Button;

.field button3:Landroid/widget/Button;

.field s:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 10
    invoke-direct {p0}, Landroid/app/Activity;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 2
    .parameter "view"

    .prologue
    .line 32

    const-string v0, "code_block"
    const-string v1, "MainActivity->onClick-BB@0x0"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p1}, Landroid/view/View;->getId()I

    move-result v0

    packed-switch v0, :pswitch_data_0

    .line 45
    :goto_0

    const-string v0, "code_block"
    const-string v1, "MainActivity->onClick-BB@0xe"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    .line 34
    :pswitch_0

    const-string v0, "code_block"
    const-string v1, "MainActivity->onClick-BB@0x10"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "button1 clicked"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    .line 35
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/example/edgexample/MainActivity;->s:Ljava/lang/String;

    goto :goto_0

    .line 38
    :pswitch_1

    const-string v0, "code_block"
    const-string v1, "MainActivity->onClick-BB@0x26"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "button2 clicked"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    .line 39
    new-instance v0, Landroid/content/Intent;

    const-class v1, Lcom/example/edgexample/BrotherActivity;

    invoke-direct {v0, p0, v1}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    invoke-virtual {p0, v0}, Lcom/example/edgexample/MainActivity;->startActivity(Landroid/content/Intent;)V

    goto :goto_0

    .line 42
    :pswitch_2

    const-string v0, "code_block"
    const-string v1, "MainActivity->onClick-BB@0x4a"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "button3 clicked"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    .line 43
    iget-object v0, p0, Lcom/example/edgexample/MainActivity;->s:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/example/edgexample/MainActivity;->s:Ljava/lang/String;

    goto :goto_0

    .line 32

    const-string v0, "code_block"
    const-string v1, "MainActivity->onClick-BB@0x6a"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    nop

    :pswitch_data_0
    .packed-switch 0x7f080002
        :pswitch_0
        :pswitch_1
        :pswitch_2
    .end packed-switch
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 1
    .parameter "savedInstanceState"

    .prologue
    .line 18
    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    .line 19
    const v0, 0x7f030001

    invoke-virtual {p0, v0}, Lcom/example/edgexample/MainActivity;->setContentView(I)V

    .line 21
    const v0, 0x7f080002

    invoke-virtual {p0, v0}, Lcom/example/edgexample/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/example/edgexample/MainActivity;->button1:Landroid/widget/Button;

    .line 22
    const v0, 0x7f080003

    invoke-virtual {p0, v0}, Lcom/example/edgexample/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/example/edgexample/MainActivity;->button2:Landroid/widget/Button;

    .line 23
    const v0, 0x7f080004

    invoke-virtual {p0, v0}, Lcom/example/edgexample/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/example/edgexample/MainActivity;->button3:Landroid/widget/Button;

    .line 25
    iget-object v0, p0, Lcom/example/edgexample/MainActivity;->button1:Landroid/widget/Button;

    invoke-virtual {v0, p0}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 26
    iget-object v0, p0, Lcom/example/edgexample/MainActivity;->button2:Landroid/widget/Button;

    invoke-virtual {v0, p0}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 27
    iget-object v0, p0, Lcom/example/edgexample/MainActivity;->button3:Landroid/widget/Button;

    invoke-virtual {v0, p0}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 28
    return-void
.end method
