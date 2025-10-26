import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const Color primaryDeepPurple = Color.fromARGB(255, 128, 0, 128);
  static const Color accentPinkish = Color.fromARGB(255, 230, 80, 160); 
  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Vangti Chai',
      debugShowCheckedModeBanner: false,
      
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: primaryDeepPurple,
          primary: primaryDeepPurple,
          secondary: accentPinkish,
        ),
        useMaterial3: true,
        fontFamily: 'monospace',
      ),
      home: const VangtiChaiHome(title: 'Vangti Chai'),
    );
  }
}

class VangtiChaiHome extends StatefulWidget {
  const VangtiChaiHome({super.key, required this.title});

  final String title;

  @override
  State<VangtiChaiHome> createState() => _VangtiChaiHomeState();
}

class _VangtiChaiHomeState extends State<VangtiChaiHome> {
  String _currentAmount = '0';
  final List<int> _takaNotes = [500, 100, 50, 20, 10, 5, 2, 1];

  Map<int, int> _changeMap = {};
  
  static const Color deepPurplePinkishText = Color.fromARGB(255, 169, 35, 189);

  @override
  void initState() {
    super.initState();
    _updateCalculation();
  }

  void _onKeypadTap(String value) {
    setState(() {
      if (value == 'C') { 
        _currentAmount = '0';
      } else {
        if (_currentAmount == '0') {
          _currentAmount = value;
        } else {
          _currentAmount += value;
        }
      }
      _updateCalculation();
    });
  }

  void _updateCalculation() {
    final amount = int.tryParse(_currentAmount) ?? 0;
    _changeMap = _calculateChange(amount);
  }

  Map<int, int> _calculateChange(int amount) {
    var remainingAmount = amount;
    final Map<int, int> change = {};

    for (var note in _takaNotes) {
      if (remainingAmount >= note) {
        final count = remainingAmount ~/ note; 
        change[note] = count;
        remainingAmount %= note;
      } else {
        change[note] = 0;
      }
    }
    return change;
  }

  Widget _buildAmountDisplay(double fontSize) {
    return Padding(
      padding: const EdgeInsets.all(1.0),
      child: Text(
        'Taka: $_currentAmount',
        style: TextStyle(
          fontSize: fontSize,
          fontWeight: FontWeight.bold,
          color: deepPurplePinkishText, 
        ),
      ),
    );
  }

  Widget _buildChangeTable(double fontSize) {
    return Container(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: _takaNotes.map((note) {
          final count = _changeMap[note] ?? 0;
          return Padding(
            padding: const EdgeInsets.symmetric(vertical: 0),
            child: Text(
              '$note: $count',
              style: TextStyle(
                fontSize: fontSize, 
                color: deepPurplePinkishText.withValues(alpha: .8),
              ),
            ),
          );
        }).toList(),
      ),
    );
  }

  Widget _buildKeypad() {
    const List<List<String>> keyRows = [
      ['1', '2', '3'],
      ['4', '5', '6'],
      ['7', '8', '9'],
    ];

    Widget buildKey(String key) {
      final isClear = key == 'C';
      
      return Expanded(
        child: Padding(
          padding: const EdgeInsets.all(4.0),
          child: ElevatedButton(
            onPressed: () => _onKeypadTap(key),
            style: ElevatedButton.styleFrom(
              backgroundColor: isClear 
                  ? MyApp.accentPinkish
                  : const Color.fromARGB(255, 220, 200, 230), 
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(100)),
              textStyle: const TextStyle(fontSize: 20, fontWeight: FontWeight.w600, fontFamily: 'monospace'),
              minimumSize: const Size.fromHeight(40),
              foregroundColor: isClear ? Colors.white : MyApp.primaryDeepPurple,
            ),
            child: Text(isClear ? 'CLEAR' : key),
          ),
        ),
      );
    }
    
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          ...keyRows.map((row) => Row(
                children: row.map((key) => buildKey(key)).toList(),
              )),

          Row(
            children: <Widget>[
              buildKey('0'),

              Expanded(
                flex: 2, 
                child: Padding(
                  padding: const EdgeInsets.all(4.0),
                  child: ElevatedButton(
                    onPressed: () => _onKeypadTap('C'), 
                    style: ElevatedButton.styleFrom(
                      backgroundColor: MyApp.accentPinkish,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(100)),
                      textStyle: const TextStyle(fontSize: 20, fontWeight: FontWeight.w600, fontFamily: 'monospace'),
                      minimumSize: const Size.fromHeight(40),
                      foregroundColor: Colors.white,
                    ),
                    child: const Text('CLEAR'),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        centerTitle: true,
        titleTextStyle: TextStyle(
          fontFamily: 'monospace',
          fontSize: 24,
          fontWeight: FontWeight.bold,
          color: Theme.of(context).colorScheme.onPrimaryContainer,
        ),
      ),
      body: SingleChildScrollView(
        child: LayoutBuilder(
          builder: (context, constraints) {
            final screenWidth = MediaQuery.of(context).size.width;
            final screenHeight = MediaQuery.of(context).size.height;
            final isPortrait = screenWidth < screenHeight;
            
            final amountFontSize = isPortrait ? 40.0 : 36.0;
            final tableFontSize = isPortrait ? 20.0 : 18.0;

            if (isPortrait || screenWidth < 600) {
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  _buildAmountDisplay(amountFontSize),
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      SizedBox(
                        width: screenWidth * 0.45, 
                        child: _buildChangeTable(tableFontSize),
                      ),
                      SizedBox(
                        width: screenWidth * 0.55,
                        child: _buildKeypad(),
                      ),
                    ],
                  ),
                ],
              );
            } else {
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  _buildAmountDisplay(amountFontSize),
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Expanded(
                        flex: 1,
                        child: _buildChangeTable(tableFontSize),
                      ),
                      Expanded(
                        flex: 2, 
                        child: _buildKeypad(),
                      ),
                    ],
                  ),
                ],
              );
            }
          },
        ),
      ),
    );
  }
}