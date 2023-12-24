var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var indexRouter = require('./routes/index');
var CashconvModels = require('@telereso/cashconv-client').io.telereso.cashconv.models;
var CashconvClient = require('@telereso/cashconv-client').io.telereso.cashconv.client;
var CoreClient = require('@telereso/cashconv-client').io.telereso.kmp.core;

CoreClient.debugLogger()

const Amount = CashconvModels.Amount
const Currency = CashconvModels.Currency
const CurrencyToJsonArray = CashconvModels.CurrencyToJsonArray

var app = express();


// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

const manager = new CashconvClient.CashconvClientManager
    .Builder()
    .withConfig(new CoreClient.Config
        .Builder("jsApi", "1.0.0")
        .shouldLogHttpRequests(true)
        .build())
    .withInMemorySettings(true)
    .build()
app.get('/', async function (req, res, next) {
    res.send("Hello World!")
});
app.get('/convert/:value/:from/:to', async function (req, res, next) {
    const amount = new CashconvModels.Amount(
        CashconvModels.Currency.Companion.from(req.params.from.toUpperCase()),
        req.params.value,
        CashconvModels.Currency.Companion.from(req.params.to.toUpperCase()),
        "-"
    )

    try {
        const converted = manager.convertAmount(amount).convertedValue
        res.send({converted});
    } catch (e) {
        // CurrencyToJsonArray()
        res.send({error: e});
    }

});

app.get('/rates', async function (req, res, next) {
    try {
        const rates = await CoreClient.Tasks.async(manager.fetchRates())
        res.send({rates});
    } catch (e) {
        // CurrencyToJsonArray()
        res.send({error: e});
    }

});
// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
