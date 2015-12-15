.class public Lcom/example/edgexample/BrotherActivity;
.super Landroid/app/Activity;
.source "BrotherActivity.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# instance fields
.field back:Landroid/widget/Button;


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 9
    invoke-direct {p0}, Landroid/app/Activity;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 2
    .parameter "view"

    .prologue
    .line 22

    const-string v0, "code_block"
    const-string v1, "BrotherActivity->onClick-BB@0x0"
    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "back clicked"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    .line 23
    invoke-virtual {p0}, Lcom/example/edgexample/BrotherActivity;->finish()V

    .line 24
    return-void
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 1
    .parameter "savedInstanceState"

    .prologue
    .line 14
    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    .line 15
    const/high16 v0, 0x7f03

    invoke-virtual {p0, v0}, Lcom/example/edgexample/BrotherActivity;->setContentView(I)V

    .line 16
    const/high16 v0, 0x7f08

    invoke-virtual {p0, v0}, Lcom/example/edgexample/BrotherActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/example/edgexample/BrotherActivity;->back:Landroid/widget/Button;

    .line 17
    iget-object v0, p0, Lcom/example/edgexample/BrotherActivity;->back:Landroid/widget/Button;

    invoke-virtual {v0, p0}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 18
    return-void
.end method
